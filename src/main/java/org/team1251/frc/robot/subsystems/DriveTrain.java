package org.team1251.frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.IMotorController;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.SpeedController;
import org.team1251.frc.robot.humanInterface.input.DrivePower;
import org.team1251.frc.robot.robotMap.DeviceConnector;
import org.team1251.frc.robot.robotMap.DeviceManager;
import org.team1251.frc.robotCore.subsystems.Subsystem;

public class DriveTrain extends Subsystem {

    private final SpeedController leftTrain;
    private final SpeedController rightTrain;

    private final WPI_TalonSRX leftBackMotorController;
    private final WPI_VictorSPX leftMiddleMotorController;
    private final WPI_VictorSPX leftFrontMotorController;

    private final WPI_TalonSRX rightBackMotorController;
    private final WPI_VictorSPX rightMiddleMotorController;
    private final WPI_VictorSPX rightFrontMotorController;

    private final DeviceManager deviceManager;
    private boolean controllerFollowMode = false;


    public DriveTrain(DeviceManager deviceManager) {

        this.deviceManager = deviceManager;
        
        leftTrain = leftBackMotorController = deviceManager.createTalonSRX(DeviceConnector.MC_DRIVE_LEFT_BACK);
        configureController(leftBackMotorController, false);

        leftMiddleMotorController = deviceManager.createVictorSPX(DeviceConnector.MC_DRIVE_LEFT_MIDDLE);
        configureController(leftMiddleMotorController, false);

        leftFrontMotorController = deviceManager.createVictorSPX(DeviceConnector.MC_DRIVE_LEFT_FRONT);
        configureController(leftFrontMotorController, false);

        rightTrain = rightBackMotorController = deviceManager.createTalonSRX(DeviceConnector.MC_DRIVE_RIGHT_BACK);
        configureController(rightBackMotorController, false);

        rightMiddleMotorController = deviceManager.createVictorSPX(DeviceConnector.MC_DRIVE_RIGHT_MIDDLE);
        configureController(rightMiddleMotorController, false);

        rightFrontMotorController = deviceManager.createVictorSPX(DeviceConnector.MC_DRIVE_RIGHT_FRONT);
        configureController(rightFrontMotorController, false);
    }

    /**
     * Applies standard configuration to a motor controller.
     */
    private void configureController(IMotorController controller, boolean isInverted) {
        controller.setInverted(isInverted);
        controller.setNeutralMode(NeutralMode.Coast);
    }

    /**
     * Enables or disables motor control following.
     *
     * @param isEnabled A boolean indicating whether or not to enable following mode. Setting follow mode to its
     *                  current state does nothing.
     */
    public void setControllerFollowMode(boolean isEnabled) {

        // See if there is a change
        if (isEnabled == controllerFollowMode) {
            // No change... bail early.
            return;
        }

        // Record the new state.
        controllerFollowMode = isEnabled;

        // See if we are turning following on or off.
        if (isEnabled) {
            // Set up following.
            leftFrontMotorController.follow(leftBackMotorController);
            leftMiddleMotorController.follow(leftBackMotorController);

            rightFrontMotorController.follow(rightBackMotorController);
            rightMiddleMotorController.follow(rightBackMotorController);
            
        } else {
            // Turn off following by explicitly setting speed on following controllers
            leftFrontMotorController.set(ControlMode.PercentOutput, 0);
            leftMiddleMotorController.set(ControlMode.PercentOutput, 0);

            rightFrontMotorController.set(ControlMode.PercentOutput, 0);
            rightMiddleMotorController.set(ControlMode.PercentOutput, 0);
        }
    }



    public void drive(DrivePower power) {
        setControllerFollowMode(true);
        leftTrain.set(power.getLeft());
        rightTrain.set(power.getRight());
    }
}
