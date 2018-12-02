package org.team1251.frc.robot.robotMap;

import org.team1251.frc.robotCore.robotMap.DeviceManager;

/**
 * This RobotMap is just a thin wrapper around the DeviceManager.  Use it to statically get port assignments.
 * For example to get the digital input/output port assigned to the BIG_SWITCH device:
 *
 *    RobotMap.deviceManager.getPort(org.team1251.frc.robot.robotMap.Devices.BIG_SWITCH, PortType.DIO)
 *
 * Don't forget to define your devices and set up their port assignments in the org.team1251.frc.robot.robotMap.Devices enum.
 */
public class RobotMap
{
    // The device manager is passed simply passed the Enum class.
    public static final DeviceManager<Device> deviceManager = new DeviceManager<>(Device.class);

    // Duplication detection must be explicitly invoked. Doing it as a static initializer makes sure it is done
    // as soon as the RobotMap is loaded.
    static {
        deviceManager.detectDuplicates();
    }
}