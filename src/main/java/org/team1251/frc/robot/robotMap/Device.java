package org.team1251.frc.robot.robotMap;

import org.team1251.frc.robotCore.robotMap.Assignment;
import org.team1251.frc.robotCore.robotMap.PortAssignable;
import org.team1251.frc.robotCore.robotMap.PortType;


/**
 * List all devices (sensors, actuators, motor controllers, etc) in this enum.
 */
public enum Device implements PortAssignable {

    // Replace with devices for your robot
    DRIVE_LEFT_A(PortType.PWM, 0),
    DRIVE_LEFT_B(PortType.PWM, 1),

    DRIVE_RIGHT_A(PortType.PWM, 2),
    DRIVE_RIGHT_B(PortType.PWM, 3);

    /**
     * Port assignment for each Device
     */
    private final Assignment assignment;

    /**
     * @param portType The port type that the device is attached to
     * @param port The port the device is attached to.
     */
    Device(PortType portType, int port) {
        this.assignment = new Assignment(portType, port);
    }

    @Override
    public Assignment getAssignment() {
        return assignment;
    }
}