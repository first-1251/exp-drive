package org.team1251.frc.robot.humanInterface.input;

import org.team1251.frc.robot.Util;

public class DualStickArcadeDriveInput extends ArcadeDriveInput {

    public DualStickArcadeDriveInput(double forwardTurnFactor, double backwardTurnFactor) {
        super(forwardTurnFactor, backwardTurnFactor);
    }

    @Override
    double getThrottleInput(HumanInput humanInput) {
        return Util.applyInputCurve(humanInput.getGamePad().ls().getVertical());
    }

    @Override
    double getTurnInput(HumanInput humanInput) {
        return Util.applyInputCurve(humanInput.getGamePad().rs().getHorizontal());
    }
}
