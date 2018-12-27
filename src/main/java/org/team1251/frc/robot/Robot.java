package org.team1251.frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.team1251.frc.robot.commands.TeleopDrive;
import org.team1251.frc.robot.humanInterface.input.*;
import org.team1251.frc.robot.robotMap.DeviceManager;
import org.team1251.frc.robot.subsystems.DriveTrain;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends TimedRobot {

    private static final double TICK_PERIOD = .01;

    private final DeviceManager deviceManager = new DeviceManager();

    private HumanInput humanInput;
    private DriveTrain driveTrain;
    private TeleopDrive teleopDrive;
    private SendableChooser<DriveInput> driveInputChooser;

    private int testTicks = 0;

    public Robot() {
        super();
        setPeriod(TICK_PERIOD);
    }

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {

        initDashboardInputs();

        humanInput = new HumanInput();
        driveTrain = new DriveTrain(deviceManager);
        teleopDrive = new TeleopDrive(driveTrain, humanInput);
    }

    private void initDashboardInputs() {

        // Create a way to choose the type of drive input.
        driveInputChooser = new SendableChooser<>();
        driveInputChooser.setName("Drive Controls");

        driveInputChooser.addDefault("Tiger Drive: Trigger Throttle", new TriggerThrottleTigerDriveInput());
        driveInputChooser.addObject("Tiger Drive: Dual Stick", new DualStickTigerDriveInput());

        driveInputChooser.addObject("Arcade: Trigger Throttle", new TriggerThrottleArcadeDriveInput());
        driveInputChooser.addObject("Arcade: Dual Stick", new DualStickArcadeDriveInput());

        driveInputChooser.addObject("Arcade: Trigger Turn", new TriggerTurnArcadeDriveInput());
        driveInputChooser.addObject("Dual Stick Tank", new TankDriveInput());

        SmartDashboard.putData("Drive Controls", driveInputChooser);
    }


    /**
     * This function is called once each time the robot enters Disabled mode.
     * You can use it to reset any subsystem information you want to clear when
     * the robot is disabled.
     */
    public void disabledInit() {

    }

    public void disabledPeriodic() {
        Scheduler.getInstance().run();
    }

    /**
     * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
     * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
     * Dashboard, remove all of the chooser code and uncomment the getString code to get the auto name from the text box
     * below the Gyro
     * <p>1
     * You can add additional auto modes by adding additional commands to the chooser code above (like the commented example)
     * or additional comparisons to the switch structure below with additional strings & commands.
     */
    public void autonomousInit() {
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.

        //if (autonomousCommand != null) autonomousCommand.cancel();
        humanInput.setDriveInput(driveInputChooser.getSelected());
        driveTrain.setDefaultCommand(teleopDrive);
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }

    @Override
    public void testInit() {
         testTicks = 0;
         driveTrain.setControllerFollowMode(false);
    }

    /**
     * This function is called periodically during test mode
     */
    @Override
    public void testPeriodic() {
        // See if the test has been reset.
        if (humanInput.isTestResetButtonPressed()) {
            testTicks = 0;
            return;
        }

        // See if the special test button is being pressed
        if (!humanInput.isTestButtonPressed()) {
            // Actively stop the current motor test.
            driveTrain.stopMotorTest();

            // Return before incrementing test ticks or elapsed time. This effectively "pauses" the test.
            return;
        }

        // Keep track of how many ticks have passed while testing is active then combine it with the tick period to
        // derive a time-based measure of how much testing time has passed.
        testTicks++;
        double elapsed = testTicks * TICK_PERIOD * 2; // Fast-forward!!

        // Announce elapsed time every 50 test ticks.
        if (testTicks % 50 == 0) {
            System.out.println("Test time elapsed: " + elapsed);
        }

        // Run different motors depending on how much time has passed.
        if (elapsed < 5) {
            // Do nothing for the first few seconds.
        } else if (elapsed < 10) {
            driveTrain.testMotor(DriveTrain.Motor.LEFT_BACK);
        } else if (elapsed < 15) {
            driveTrain.stopMotorTest();
        } else if (elapsed < 20) {
            driveTrain.testMotor(DriveTrain.Motor.LEFT_MIDDLE);
        } else if (elapsed < 25) {
            driveTrain.stopMotorTest();
        } else if (elapsed < 30) {
            driveTrain.testMotor(DriveTrain.Motor.LEFT_FRONT);
        } else if (elapsed < 35) {
            driveTrain.stopMotorTest();
        } else if (elapsed < 40) {
            driveTrain.testMotor(DriveTrain.Motor.RIGHT_BACK);
        } else if (elapsed < 45) {
            driveTrain.stopMotorTest();
        } else if (elapsed < 50) {
            driveTrain.testMotor(DriveTrain.Motor.RIGHT_MIDDLE);
        } else if (elapsed < 55) {
            driveTrain.stopMotorTest();
        }  else if (elapsed < 60) {
            driveTrain.testMotor(DriveTrain.Motor.RIGHT_FRONT);
        } else {
            // We haven't reached our minimum elapsed testing time or have exceeded the maximum testing time.
            // In either case, explicitly stop the motor test.
            driveTrain.stopMotorTest();
        }
    }

}