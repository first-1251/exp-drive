package org.team1251.frc.robot.humanInterface.input;

/**
 * How much power to provide the left and right sides of the drive train.
 */
public class DrivePower {

    private final double left;
    private final double right;

    public DrivePower(double left, double right) {
        // Clamp the values at -1 and 1
        this.left = Math.max(Math.min(1, left), -1);
        this.right = Math.max(Math.min(1, right), -1);
    }

    public double getRight() {
        return right;
    }

    public double getLeft() {
        return left;
    }

    public String toString() {
        return "Drive Power: " + left + "|" + right;
    }
}
