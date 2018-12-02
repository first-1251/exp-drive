package org.team1251.frc.robot.humanInterface.input;

public class TankDriveInput implements DriveInput {

    @Override
    public DrivePower getDrivePower(HumanInput humanInput) {
        return new DrivePower(humanInput.getGamePad().ls().getVertical(), humanInput.getGamePad().rs().getVertical());
    }
}
