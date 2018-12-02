package org.team1251.frc.robot.humanInterface.input;

abstract class ArcadeDriveInput implements DriveInput {

    abstract double getThrottleInput(HumanInput humanInput);
    abstract double getTurnInput(HumanInput humanInput);

    @Override
    public DrivePower getDrivePower(HumanInput humanInput) {
        double throttleInput = getThrottleInput(humanInput);
        double turnInput = getTurnInput(humanInput);

        // Calculate the dampened throttle. If the original input was negative, negate the dampened value.
        double dampenedThrottle = calculateTurnThrottle(Math.abs(throttleInput), Math.abs(turnInput));
        if (throttleInput < 0) {
            dampenedThrottle = -dampenedThrottle;
        }

        if (throttleInput == 0) {
            return new DrivePower(-turnInput, turnInput);
        }

        double leftPower = throttleInput;
        double rightPower = throttleInput;


        if (turnInput < 0) {
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
