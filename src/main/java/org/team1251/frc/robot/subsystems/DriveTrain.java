package org.team1251.frc.robot.subsystems;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Talon;
import org.team1251.frc.robot.humanInterface.input.DrivePower;
import org.team1251.frc.robot.robotMap.DeviceConnector;
import org.team1251.frc.robot.robotMap.DeviceManager;
import org.team1251.frc.robotCore.subsystems.Subsystem;

public class DriveTrain extends Subsystem {

    private final SpeedControllerGroup leftTrain;
    private final SpeedControllerGroup rightTrain;
    private final DeviceManager deviceManager;

    public DriveTrain(DeviceManager deviceManager) {

        this.deviceManager = deviceManager;

        leftTrain = new SpeedControllerGroup(
            initController(DeviceConnector.DRIVE_LEFT_A, false),
            initController(DeviceConnector.DRIVE_LEFT_B, false)
        );

        rightTrain = new SpeedControllerGroup(
                initController(DeviceConnector.DRIVE_RIGHT_A, true),
                initController(DeviceConnector.DRIVE_RIGHT_B, true)
        );
    }

    private Talon initController(DeviceConnector motorConnector, boolean isInverted) {
        Talon controller = deviceManager.createTalon(motorConnector);
        controller.setInverted(isInverted);
        return controller;
    }

    public void drive(DrivePower power) {
        leftTrain.set(power.getLeft());
        rightTrain.set(power.getRight());
    }
}
