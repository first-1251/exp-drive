package org.team1251.frc.robot.humanInterface.input;

public abstract class ArcadeDriveInput implements DriveInput {

    class InputValues {
        final double throttle;
        final double turn;
        final boolean isQuickTurn;

        InputValues(double throttle, double turn, boolean isQuickTurn) {
            this.throttle = throttle;
            this.turn = turn;
            this.isQuickTurn = isQuickTurn;
        }
    }

    InputValues getInputValues(HumanInput humanInput) {

        // Get the direct throttle/turn inputs
        double throttle = getThrottleInput(humanInput);
        double turn = getTurnInput(humanInput);

        // If throttle is very low, trigger a quick-turn
        boolean isQuickTurn = Math.abs(throttle) <= .05;

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
