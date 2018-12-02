package org.team1251.frc.robot.subsystems;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Talon;
import org.team1251.frc.robot.Motor;
import org.team1251.frc.robot.humanInterface.input.DrivePower;
import org.team1251.frc.robot.robotMap.RobotMap;
import org.team1251.frc.robotCore.robotMap.PortType;
import org.team1251.frc.robotCore.subsystems.NoInitDefaultCmdSubsystem;

public class DriveTrain extends NoInitDefaultCmdSubsystem {

    private final SpeedControllerGroup leftTrain;
    private final SpeedControllerGroup rightTrain;

    public DriveTrain() {

        leftTrain = new SpeedControllerGroup(
            initController(Motor.LEFT_A),
            initController(Motor.LEFT_B)
        );

        rightTrain = new SpeedControllerGroup(
                initController(Motor.RIGHT_A),
                initController(Motor.RIGHT_B)
        );
    }

    private static Talon initController(Motor motor) {
        System.out.println("Port: " + RobotMap.deviceManager.getPort(motor.getDevice(), PortType.PWM));
        Talon controller = new Talon(RobotMap.deviceManager.getPort(motor.getDevice(), PortType.PWM));
        controller.setInverted(motor.isInverted());
        return controller;
    }

    public void drive(DrivePower power) {
        leftTrain.set(power.getLeft());
        rightTrain.set(power.getRight());
    }
}
