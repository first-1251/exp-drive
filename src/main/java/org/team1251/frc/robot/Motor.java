package org.team1251.frc.robot;

import org.team1251.frc.robot.robotMap.Device;

/**
 * Used to identify/select a specific motor.
 */
public enum Motor {

    LEFT_A(false, Device.DRIVE_LEFT_A),
    LEFT_B(false, Device.DRIVE_LEFT_B),

    RIGHT_A(true, Device.DRIVE_RIGHT_A),
    RIGHT_B(true, Device.DRIVE_RIGHT_B);

    private final boolean inverted;

    public boolean isInverted() {
        return inverted;
    }

    public Device getDevice() {
        return device;
    }

    private final Device device;

    Motor(boolean inverted, Device device) {
        this.inverted = inverted;
        this.device = device;
    }
}
