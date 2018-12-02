package org.team1251.frc.robot.humanInterface.input;

/**
 * How much power to provide the left and right sides of the drive train.
 */
public class DrivePower {

    private final double left;
    private final double right;

    public DrivePower(double left, double right) {
        this.left = left;
        this.right = right;
    }

    public double getRight() {
        return right;
    }

    public double getLeft() {
        return left;
    }
}
