package org.team1251.frc.robot.humanInterface.input;

public class TriggerThrottleArcadeDriveInput extends ArcadeDriveInput {

    @Override
    double getThrottleInput(HumanInput humanInput) {
        return humanInput.getGamePad().lt().getValue() - humanInput.getGamePad().rt().getValue();
    }

    @Override
    double getTurnInput(HumanInput humanInput) {
        return humanInput.getGamePad().ls().getHorizontal();
    }
}
