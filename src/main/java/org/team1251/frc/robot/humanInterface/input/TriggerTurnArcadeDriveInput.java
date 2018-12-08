package org.team1251.frc.robot.humanInterface.input;

import org.team1251.frc.robot.Util;

public class TriggerTurnArcadeDriveInput extends ArcadeDriveInput {

    private final Util.ValueSmoother throttleSmoother;

    public TriggerTurnArcadeDriveInput() {
        super();
        throttleSmoother = new Util.ValueSmoother(3, true);

        // Less range on the triggers, so require more turn power before we are willing to do an in-motion quick-turn.
        movingQuickTurnThreshold = .98;
    }

    @Override
    double getThrottleInput(HumanInput humanInput) {
        // Smooth the value coming off the stick to soften dramatic changes and then pass the result through
        // the input curve function.
        return Util.applyInputCurve(
                throttleSmoother.getSmoothedValue(humanInput.getGamePad().ls().getVertical()), .75, 6
        );
    }

    @Override
    double getTurnInput(HumanInput humanInput) {
        // Right turn value less the left turn value gives us the net turn value. If left turn value is bigger, we'll
        // end up with a negative number which is what we want.
        return Util.applyInputCurve(humanInput.getGamePad().rt().getValue() - humanInput.getGamePad().lt().getValue(), .75, 6);

    }
}
