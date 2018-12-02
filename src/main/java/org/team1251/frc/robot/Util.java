package org.team1251.frc.robot;

public class Util {

    /**
     * Squares an input value, preserving the original sign.
     */
    public static double applyInputCurve(double value) {
        double refined = Math.sin(Math.pow(value, 2) * Math.PI/2);
        return value < 0 ? -refined : refined;
    }
}
