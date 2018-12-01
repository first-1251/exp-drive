package org.team1251.frc.robot.robotMap;

import org.team1251.frc.robotCore.robotMap.Assignment;
import org.team1251.frc.robotCore.robotMap.Device;
import org.team1251.frc.robotCore.robotMap.PortType;


/**
 * List all devices (sensors, actuators, motor controllers, etc) in this enum.
 */
public enum Devices implements Device {

    // Replace with devices for your robot
    SAMPLE_SWITCH(PortType.DIO, 0);

    /**
     * Port assignment for each Device
     */
    private final Assignment assignment;

    /**
     * @param portType The port type that the device is attached to
     * @param port The port the device is attached to.
     */
    Devices(PortType portType, int port) {
        this.assignment = new Assignment(portType, port);
    }

    @Override
    public Assignment getAssignment() {
        return assignment;
    }
}