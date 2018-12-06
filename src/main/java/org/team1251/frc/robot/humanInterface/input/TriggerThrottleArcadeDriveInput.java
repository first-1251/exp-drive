package org.team1251.frc.robot.humanInterface.input;

import org.team1251.frc.robot.Util;

public class TriggerThrottleArcadeDriveInput extends ArcadeDriveInput {

    private final Util.ValueSmoother throttleSmoother;

    public TriggerThrottleArcadeDriveInput() {
        super();
        throttleSmoother = new Util.ValueSmoother(3, true);
    }

    @Override
    double getThrottleInput(HumanInput humanInput) {
        // Raw input value is the net result of forward power and backward power. Run the calculated throttle
        // through the smoother to soften dramatic changes and then pass the result through the input curve function.
        return Util.applyInputCurve(
                throttleSmoother.getSmoothedValue(
                        humanInput.getGamePad().lt().getValue() - humanInput.getGamePad().rt().getValue()
                ), .75, 6
        );
    }

    @Override
    double getTurnInput(HumanInput humanInput) {
        return Util.applyInputCurve(humanInput.getGamePad().ls().getHorizontal(), .75, 6);
    }
}
