package org.team1251.frc.robot.humanInterface.input;

import org.team1251.frc.robot.Util;
import org.team1251.frc.robotCore.util.ValueSmoother;

public class TriggerTurnArcadeDriveInput extends ArcadeDriveInput {

    private final ValueSmoother throttleSmoother;

    public TriggerTurnArcadeDriveInput() {
        super();
        throttleSmoother = new ValueSmoother(3);
    }

    @Override
    InputValues getInputValues(HumanInput humanInput) {

        // Get the direct throttle/turn inputs
        double throttle = getThrottleInput(humanInput);
        double turn = getTurnInput(humanInput);

        // If turn power is higher than 92% in either direction, force a quick-turn
        boolean isQuickTurn = Math.abs(turn) > .92;

        // Put it all together and return it.
        return new InputValues(throttle, turn, isQuickTurn);
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

        // TODO: Apply input curve?

        // Right turn value less the left turn value gives us the net turn value. If left turn value is bigger, we'll
        // end up with a negative number which is what we want.
        return humanInput.getGamePad().rt().getValue() - humanInput.getGamePad().lt().getValue();

    }
}
