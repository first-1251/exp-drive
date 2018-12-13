package org.team1251.frc.robot.humanInterface.input;

public abstract class TigerDriveInput implements DriveInput {

    /** 
     * Used to calculate the turn power based on turn and throttle inputs.
     * 
     * At full throttle and full turn, this is the difference in power between the left and
     * right sides of the drive train.  
     * 
     * For two input samples with the full turn value, the one with less throttle input will result
     * in a drive power difference which is HIGHER than this value.
     * 
     * For two input samples with full throttle value, the one with less turn input will result
     * in a drive power difference which is LOWER than this value.
     */
    double turnSensitivity = .85;

    double reverseQuickturnThreshold = .10;

    /** The minimum turn power (in either direction) required to trigger a quick-turn. */
    double quickTurnThreshold = .15;

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

    private InputValues getInputValues(HumanInput humanInput) {

        // Get the direct throttle/turn inputs
        double throttle = getThrottleInput(humanInput);
        double turn = getTurnInput(humanInput);

        return new InputValues(throttle, turn, Math.abs(turn) >= quickTurnThreshold && isQuickTurnButtonPressed(humanInput));
    }

    abstract boolean isQuickTurnButtonPressed(HumanInput humanInput);

    abstract double getThrottleInput(HumanInput humanInput);

    abstract double getTurnInput(HumanInput humanInput);

    @Override
    public DrivePower getDrivePower(HumanInput humanInput) {
        InputValues input = getInputValues(humanInput);

        // Calculate the maximum power adjustment to be made to the right and left drive trains based on driver inputs.
        // Less throttle (in either direction) or less turn will result in a smaller value. This adjustment is added to
        // one side of the drive base and removed from the other. The sign of the adjustment is based on the sign of the
        // turn input (e.g. if turning left, the adjustment will be negative).
        //
        // NOTE: Even though this makes it look like you get more dramatic turns at high speed, that is not the case! See 
        // more details on that point down below where the adjustment is made.
        double turningPowerAdjustment = input.turn * input.throttle;
        
        // See if this is a quick-turn scenario.
        if (input.isQuickTurn) {
            // If the driver is trying to go backwards, then invert the quickturn directio to keep things
            // more natural.  (Note: throttle > 0 means backwards... because reasons)
            double quickTurn = input.throttle > reverseQuickturnThreshold ? -input.turn : input.turn;

            // Set the sides of the drive train to exact opposites of each other.
            return new DrivePower(-quickTurn, quickTurn);
        }

        // Apply the adjustment. Always add it to the right side and remove it from the left side. The sign of the adjustment
        // will account for turn direction.
        //
        // NOTE: At higher throttles, one side will exceed the 1,-1 boundary. This will DECREASE the total difference between
        //       the sides of the drive trains, thus producing LESS dramatic turns at higher throttle. By contrast, the slower
        //       drive train never drops below zero (even at low throttle) because the turn adjustment power can never exceed 
        //       the turn throttle since the formula is `turn * throttle` -- at a turn value of 1 (maximum), the turning
        //       adjustment would be equal to the throttle.
        return new DrivePower(input.throttle + turningPowerAdjustment, input.throttle - turningPowerAdjustment);

    }    
}
