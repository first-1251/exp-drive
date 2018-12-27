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

    /**
     * The minimum reverse throttle input value required to invert the quick-turn direction. Quick-turn is inverted when
     * going backwards to make the motion consistent with regular turning while moving backwards.
     *
     * This should always be a negative value since it represents an amount of reverse throttle power. The primary use
     * for this is as a "noise filter", so practical values are slightly below 0.
     *
     * A value of zero (or any positive value) will invert the quick-turn direction if *any* reverse throttle input
     * value is detected. A value of -1 (or less) will *never* invert quick turn direction.
     */
    double reverseQuickturnThreshold = -.10;

    /**
     * The minimum turn input value (in either direction) required for quick-turns.
     *
     * This should always be a positive value since it represents the turn inputs distance from 0. The primary use
     * for is to filter out "noise" and values that would not turn the robot due to friction. The perfect value is:
     *   - High enough to ignore input "noise"
     *   - High enough to ignore turn values which will overcome friction (wasted motor power)
     *   - Low enough to achieve the minimum desired quick-turn speed.
     *
     * A value of zero (or any negative value) will always allow a quick turn. A value of 1 (or higher) will never
     * allow a quick turn.
     */
    double quickTurnThreshold = .15;

    /**
     * Local representation of interpreted input values.
     */
    class InputValues {
        /**
         * Value between -1 and 1 representing throttle input value where -1 is full speed backwards and 1 is
         * full speed forward.
         */
        final double throttle;

        /**
         * Value between -1 and 1 representing turning input value where -1 is full left turn and 1 is full
         * right turn.
         */
        final double turn;

        /** Represents whether or not a quick turn has been activated. */
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

    /**
     * Helper method for packaging throttle, turn, and quick turn inputs into an InputValues instance.
     *
     * @param humanInput The source of the input values.
     */
    private InputValues getInputValues(HumanInput humanInput) {

        // Get the direct throttle/turn inputs
        double throttle = getThrottleInput(humanInput);
        double turn = getTurnInput(humanInput);

        // Package inputs up. The quick turn is a matter of the turn threshold being reached and an explicit
        // button press.
        return new InputValues(throttle, turn, Math.abs(turn) >= quickTurnThreshold && isQuickTurnButtonPressed(humanInput));
    }

    /**
     * Provides an indication of whether quick turn has been requested by the human player.
     */
    abstract boolean isQuickTurnButtonPressed(HumanInput humanInput);

    /**
     * Provides the throttle input value requested by the human player.
     *
     * @return A value between -1 and 1 where -1 is full backward speed and 1 is full forward speed.
     */
    abstract double getThrottleInput(HumanInput humanInput);

    /**
     * Provides the turn input value requested by the human player.
     *
     * @return A value between -1 and 1 where -1 is full left turn and 1 is full right turn.
     */
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
            // If the driver is trying to go backwards, then invert the quick-turn direction to keep things
            // more natural.
            double quickTurn = input.throttle < reverseQuickturnThreshold ? -input.turn : input.turn;

            // Set the sides of the drive train to exact opposites of each other.
            return new DrivePower(quickTurn, -quickTurn);
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
