package org.team1251.frc.robot.humanInterface.input;

public class DualStickArcadeDriveInput extends ArcadeDriveInput {

    public DualStickArcadeDriveInput(double forwardTurnFactor, double backwardTurnFactor) {
        super(forwardTurnFactor, backwardTurnFactor);
    }

    @Override
    double getThrottleInput(HumanInput humanInput) {
        return humanInput.getGamePad().ls().getVertical();
    }

    @Override
    double getTurnInput(HumanInput humanInput) {
        return humanInput.getGamePad().rs().getHorizontal();
    }
}
