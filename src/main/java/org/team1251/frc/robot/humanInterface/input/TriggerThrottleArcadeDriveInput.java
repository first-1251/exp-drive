package org.team1251.frc.robot.humanInterface.input;

public class TriggerThrottleArcadeDriveInput extends ArcadeDriveInput {

    public TriggerThrottleArcadeDriveInput(double forwardTurnFactor, double backwardTurnFactor) {
        super(forwardTurnFactor, backwardTurnFactor);
    }

    @Override
    double getThrottleInput(HumanInput humanInput) {
        return humanInput.getGamePad().lt().getValue() - humanInput.getGamePad().rt().getValue();
    }

    @Override
    double getTurnInput(HumanInput humanInput) {
        return humanInput.getGamePad().ls().getHorizontal();
    }
}
