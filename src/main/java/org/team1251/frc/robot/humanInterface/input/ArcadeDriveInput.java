package org.team1251.frc.robot.humanInterface.input;

public abstract class ArcadeDriveInput implements DriveInput {

    private double forwardTurnFactor;
    private double backwardTurnFactor;

    public void setTurnFactor(double forwardTurnFactor, double backwardTurnFactor) {
        this.forwardTurnFactor = forwardTurnFactor;
        this.backwardTurnFactor = backwardTurnFactor;
    }

    ArcadeDriveInput(double forwardTurnFactor, double backwardTurnFactor) {
        this.forwardTurnFactor = Math.min(1, Math.max(0, forwardTurnFactor));
        this.backwardTurnFactor = Math.min(1, Math.max(0, backwardTurnFactor));
    }

    abstract double getThrottleInput(HumanInput humanInput);
    abstract double getTurnInput(HumanInput humanInput);

    @Override
    public DrivePower getDrivePower(HumanInput humanInput) {
        double throttleInput = getThrottleInput(humanInput);
        double turnInput = getTurnInput(humanInput);

        // If near to no throttle, do an in-place turn using the turn input. This fixes a problem where
        // some noise on the line will rarely prevent the quick-turn logic from triggering.
        if (Math.abs(throttleInput) <= .05) {
            return new DrivePower(-turnInput, turnInput);
        }

        // Apply appropriate turn factor depending on if the robot is going forward or backward.
        if (throttleInput < 0) {
            turnInput *= backwardTurnFactor;
        } else {
            turnInput *= forwardTurnFactor;
        }

        // Calculate the dampened throttle. If the original input was negative, negate the dampened value.
        double dampenedThrottle = calculateTurnThrottle(Math.abs(throttleInput), Math.abs(turnInput));
        if (throttleInput < 0) {
            dampenedThrottle = -dampenedThrottle;
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
