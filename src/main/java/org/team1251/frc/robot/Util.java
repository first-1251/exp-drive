package org.team1251.frc.robot;

public class Util {

    /**
     * Applies a curve to an input value (as seen here: https://www.desmos.com/calculator/scr5agfmia).
     *
     * @param value The raw input value.
     * @param dampeningStrength A value between 0 and 1. A lower value applies less dampening to low input values.
     * @param dampeningRangeFactor Dampening applies heavier to lower input values. A higher dampeningRangeFactor causes
     *                             heavier dampening to apply to a wider range of input values. At a value of 1 the
     *                             range is empty thus no dampening occurs. With a maximum dampeningStrength (1):
     *                                - A dampeningRangeFactor of 2 reduces an input of .05 to about .25
     *                                - A dampeningRangeFactor of 3 reduces an input of .05 to about .125
     *                                - A dampeningRangeFactor of 9 reduces an input of .05 to below .002
     */
    public static double applyInputCurve(double value, double dampeningStrength, int dampeningRangeFactor) {
        // Do the math, preserve the original sign.
        //
        double refined = (dampeningStrength * Math.pow(value, dampeningRangeFactor)) + ((1 - dampeningStrength) * value);
        return value < 0 ? -refined : refined;
    }
}
