package org.team1251.frc.robot.humanInterface.input;

import org.team1251.frc.robot.Util;

public class DualStickTigerDriveInput extends TigerDriveInput {

    @Override
    boolean isQuickTurnButtonPressed(HumanInput humanInput) {
        return humanInput.getGamePad().rt().isPressed();
    }

    @Override
    double getThrottleInput(HumanInput humanInput) {
        // Just dealing with literal values here, no smoothing, no input curve.
        return humanInput.getGamePad().ls().getVertical();
    }

    @Override
    double getTurnInput(HumanInput humanInput) {
        // TODO: Maybe sine curve is more appropriate for turning?
        // Pass the raw turn value through an input curve, then apply the turn sensitivity.
        return Util.applyInputCurve(humanInput.getGamePad().rs().getHorizontal(), .75, 3) * turnSensitivity;
    }
}
