package org.team1251.frc.robot.humanInterface.input;

import org.team1251.frc.robot.Util;

public class TriggerThrottleArcadeDriveInput extends ArcadeDriveInput {

    public TriggerThrottleArcadeDriveInput(double forwardTurnFactor, double backwardTurnFactor) {
        super(forwardTurnFactor, backwardTurnFactor);
    }

    @Override
    double getThrottleInput(HumanInput humanInput) {
        return Util.applyInputCurve(humanInput.getGamePad().lt().getValue()) -
                Util.applyInputCurve(humanInput.getGamePad().rt().getValue());
    }

    @Override
    double getTurnInput(HumanInput humanInput) {
        return Util.applyInputCurve(humanInput.getGamePad().ls().getHorizontal());
    }
}
