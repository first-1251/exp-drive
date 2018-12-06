package org.team1251.frc.robot.humanInterface.input;

import org.team1251.frc.robot.Util;

public class SingleStickArcadeDriveInput extends ArcadeDriveInput {

    private final Util.ValueSmoother throttleSmoother;

    public SingleStickArcadeDriveInput() {
        super();
        throttleSmoother = new Util.ValueSmoother(3, true);
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
        return Util.applyInputCurve(humanInput.getGamePad().ls().getHorizontal(), .75, 6);
    }
}
