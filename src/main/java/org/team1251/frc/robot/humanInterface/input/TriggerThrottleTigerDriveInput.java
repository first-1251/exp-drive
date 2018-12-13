package org.team1251.frc.robot.humanInterface.input;

import org.team1251.frc.robot.Util;

public class TriggerThrottleTigerDriveInput extends TigerDriveInput {

    @Override
    boolean isQuickTurnButtonPressed(HumanInput humanInput) {
        return humanInput.getGamePad().x().isPressed();
    }

    @Override
    double getThrottleInput(HumanInput humanInput) {
        // Trigger-based throttle frees up right thumb for face-buttons.
        // Just dealing with literal values here, no smoothing, no input curve.
        // TODO: This seems backwards because negative values move us forward. Do necessary inversions to make this logical!
        return humanInput.getGamePad().lt().getValue() - humanInput.getGamePad().rt().getValue();
    }

    @Override
    double getTurnInput(HumanInput humanInput) {
        // TODO: Maybe sine curve is more appropriate for turning?
        // Pass the raw turn value through an input curve, then apply the turn sensitivity.
        return Util.applyInputCurve(humanInput.getGamePad().ls().getHorizontal(), .75, 3) * turnSensitivity;
    }
}
