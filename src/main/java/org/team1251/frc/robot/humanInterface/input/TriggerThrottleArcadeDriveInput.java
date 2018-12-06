package org.team1251.frc.robot.humanInterface.input;

import org.team1251.frc.robot.Util;
import org.team1251.frc.robotCore.util.ValueSmoother;

public class TriggerThrottleArcadeDriveInput extends ArcadeDriveInput {

    private final ValueSmoother throttleSmoother;

    public TriggerThrottleArcadeDriveInput() {
        super();
        throttleSmoother = new ValueSmoother(3);
    }

    @Override
    double getThrottleInput(HumanInput humanInput) {
        // Raw input value is the net result of forward power and backward power. Run the calculated throttle
        // through the smoother to soften dramatic changes and then pass the result through the input curve function.
        return Util.applyInputCurve(
                throttleSmoother.getSmoothedValue(
                        humanInput.getGamePad().lt().getValue() - humanInput.getGamePad().rt().getValue()
                )
        );
    }

    @Override
    double getTurnInput(HumanInput humanInput) {
        return Util.applyInputCurve(humanInput.getGamePad().ls().getHorizontal());
    }
}
