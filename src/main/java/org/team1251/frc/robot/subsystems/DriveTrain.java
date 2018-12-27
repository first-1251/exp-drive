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

    private Motor motorInTesting = null;

    private final DeviceManager deviceManager;
    private boolean controllerFollowMode = false;

    public enum Motor {
        LEFT_BACK, LEFT_MIDDLE, LEFT_FRONT,
        RIGHT_BACK, RIGHT_MIDDLE, RIGHT_FRONT
    }


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

    /**
     * Stops the motor which is currently being tested.
     */
    public void stopMotorTest() {
        // Nothing to stop
        if (motorInTesting == null) {
            return;
        }

        // Figure out which controller to use and stop the motor.
        deriveController(motorInTesting).set(ControlMode.PercentOutput, 0);
        System.out.println("Stopping motor test on: " + motorInTesting.name());
        motorInTesting = null;
    }


    /**
     * Provides the motor controller which is controlling the given motor.
     */
    private IMotorController deriveController(Motor motor) {

        IMotorController controller;
        switch (motor) {
            case LEFT_BACK:
                controller = leftBackMotorController;
                break;

            case LEFT_MIDDLE:
                controller = leftMiddleMotorController;
                break;

            case LEFT_FRONT:
                controller = leftFrontMotorController;
                break;

            case RIGHT_BACK:
                controller = rightBackMotorController;
                break;

            case RIGHT_MIDDLE:
                controller = rightMiddleMotorController;
                break;

            case RIGHT_FRONT:
                controller = rightFrontMotorController;
                break;

            default:
                throw new RuntimeException("No controller associated with motor: " + motor);
        }

        return controller;
    }

    /**
     * Starts or stops a motor. When motor is running, it runs at 50% forward power.
     *
     * If testing for a different motor is already in progress, it will be stopped before starting the test for
     * this motor.
     */
    public void testMotor(Motor motor) {

        // Explicitly turn off following mode so that motors can be controlled individually.
        setControllerFollowMode(false);

        // Do nothing, if this motor is already being tested.
        if (motor == motorInTesting) {
            return;
        }

        // Stop whatever motor was previously being tested and mark this one as the one currently being tested.
        stopMotorTest();
        motorInTesting = motor;

        // Figure out which controller to use and run the motor at 50% forward power.
        deriveController(motor).set(ControlMode.PercentOutput, .5);
        System.out.println("Starting motor test on: " + motor.name());
    }

    public void drive(DrivePower power) {
        setControllerFollowMode(true);
        leftTrain.set(power.getLeft());
        rightTrain.set(power.getRight());
    }
}
