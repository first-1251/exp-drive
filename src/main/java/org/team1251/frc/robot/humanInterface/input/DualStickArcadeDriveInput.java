package org.team1251.frc.robot.humanInterface.input;

public class DualStickArcadeDriveInput extends ArcadeDriveInput {


    @Override
    double getThrottleInput(HumanInput humanInput) {
        return humanInput.getGamePad().ls().getVertical();
    }

    @Override
    double getTurnInput(HumanInput humanInput) {
        return humanInput.getGamePad().rs().getHorizontal();
    }
}
