package org.team1251.frc.robot.humanInterface.input;

public abstract class ArcadeDriveInput implements DriveInput {

    /**
     * Overridable turn power threshold at which point quick turn kicks in _while_ in motion.
     *
     * For example, with the default value, a turn equal to or greater than 95% will force a quick turn.
     */
    double movingQuickTurnThreshold = .92;

    class InputValues {
        final double throttle;
        final double turn;
        final boolean isQuickTurn;

        InputValues(double throttle, double turn, boolean isQuickTurn) {
            this.throttle = throttle;
            this.turn = turn;
            this.isQuickTurn = isQuickTurn;
        }

        @Override
        public String toString() {
            return throttle + "|" + turn + "|" + (isQuickTurn ? "true" : "false");
        }
    }

    InputValues getInputValues(HumanInput humanInput) {

        // Get the direct throttle/turn inputs
        double throttle = getThrottleInput(humanInput);
        double turn = getTurnInput(humanInput);

        // Quick turn happens when there is turning power applied but (near) no throttle... but it can also be
        // forced when there is throttle power if turn power exceeds the threshold.
        double turnPower = Math.abs(turn);
        double throttlePower = Math.abs(throttle);
        boolean isNearZeroThrottle = throttlePower < 0.05;

        boolean isQuickTurn = false;
        if (turnPower > 0) {
            // There is turn power, so there is a couple of opportunities for quick turn to become active.
            if (isNearZeroThrottle) {
                // Condition 1: There is near-zero throttle.
                isQuickTurn = true;
            } else if (turnPower >= movingQuickTurnThreshold) {
                // Condition 2: Throttle is NOT near zero, but we've crossed the threshold for quick turn while moving.
                isQuickTurn = true;

                // In this case, we should invert the turning input if when moving backwards. Here we are dealing
                // with the actual throttle and turn input values instead of the absolute "power" values.
                // TODO: Disable inverted input y-axis and reverse both sets of motors to make this check natural!
                if (throttle > 0) {
                    // Moving backwards, invert the turn direction.
                    turn = -turn;
                }
            }
        }

        // Put it all together and return it.
        return new InputValues(throttle, turn, isQuickTurn);
    }

    abstract double getThrottleInput(HumanInput humanInput);
    abstract double getTurnInput(HumanInput humanInput);

    @Override
    public DrivePower getDrivePower(HumanInput humanInput) {
        InputValues input = getInputValues(humanInput);

        // Ignore throttle in quick-turn scenarios.
        if (input.isQuickTurn) {
            // TODO: consider adjusting based on current throttle and/or current velocity (not currently available)
            return new DrivePower(-input.turn, input.turn);
        }

        // Calculate the dampened throttle. If the original input was negative, negate the dampened value.
        double dampenedThrottle = calculateTurnThrottle(Math.abs(input.throttle), Math.abs(input.turn));
        if (input.throttle < 0) {
            dampenedThrottle = -dampenedThrottle;
        }



        double leftPower = input.throttle;
        double rightPower = input.throttle;


        if (input.turn < 0) {
            // Throttle left drive
            leftPower = dampenedThrottle;
        } else {
            // Throttle right drive
            rightPower = dampenedThrottle;
        }

        return new DrivePower(leftPower, rightPower);

    }

    private double calculateTurnThrottle(double throttle, double turn) {
        return (1 - turn) * throttle;
    }
}
