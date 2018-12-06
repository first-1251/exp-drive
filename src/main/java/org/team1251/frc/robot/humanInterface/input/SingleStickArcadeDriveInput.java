package org.team1251.frc.robot.humanInterface.input;

import org.team1251.frc.robot.Util;
import org.team1251.frc.robotCore.util.ValueSmoother;

public class SingleStickArcadeDriveInput extends ArcadeDriveInput {

    private final ValueSmoother throttleSmoother;

    public SingleStickArcadeDriveInput() {
        super();
        throttleSmoother = new ValueSmoother(3);
    }

    @Override
    double getThrottleInput(HumanInput humanInput) {
        // Smooth the value coming off the stick to soften dramatic changes and then pass the result through
        // the input curve function.
        return Util.applyInputCurve(
                throttleSmoother.getSmoothedValue(humanInput.getGamePad().ls().getVertical())
        );
    }

    @Override
    double getTurnInput(HumanInput humanInput) {
        return Util.applyInputCurve(humanInput.getGamePad().ls().getHorizontal());
    }
}
