package org.team1251.frc.robot.humanInterface.input;

import org.team1251.frc.robot.Util;

public class TankDriveInput implements DriveInput {

    @Override
    public DrivePower getDrivePower(HumanInput humanInput) {
        return new DrivePower(
                Util.applyInputCurve(humanInput.getGamePad().ls().getVertical(), .75, 6),
                Util.applyInputCurve(humanInput.getGamePad().rs().getVertical(), .75, 6)
        );
    }
}
