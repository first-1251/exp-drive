package org.team1251.frc.robot.humanInterface.input;

import org.team1251.frc.robot.Util;

public class TriggerTurnArcadeDriveInput extends ArcadeDriveInput {

    private final Util.ValueSmoother throttleSmoother;

    public TriggerTurnArcadeDriveInput() {
        super();
        throttleSmoother = new Util.ValueSmoother(3, true);
    }

    @Override
    InputValues getInputValues(HumanInput humanInput) {

        // Get the direct throttle/turn inputs
        double throttle = getThrottleInput(humanInput);
        double turn = getTurnInput(humanInput);

        // Quick turn happens when there is turning power applied but (near) no throttle... but it can also be
        // forced if turn power exceeds 92%, even when moving.
        double turnPower = Math.abs(turn);
        boolean isQuickTurn = turnPower > .92 || Math.abs(throttle) <= 0.05 && turnPower > .05;

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
