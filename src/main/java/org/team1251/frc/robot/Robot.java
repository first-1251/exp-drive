package org.team1251.frc.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
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
public class Robot extends IterativeRobot {

    private final DeviceManager deviceManager = new DeviceManager();

    private HumanInput humanInput;
    private DriveTrain driveTrain;
    private TeleopDrive teleopDrive;
    private SendableChooser<DriveInput> driveInputChooser;

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

        driveInputChooser.addDefault(
                "Arcade: Dual Stick",
                new DualStickArcadeDriveInput()
        );

        driveInputChooser.addObject(
                "Arcade: Trigger Throttle",
                new TriggerThrottleArcadeDriveInput()
        );

        driveInputChooser.addDefault(
                "Arcade: Single Stick",
                new SingleStickArcadeDriveInput()
        );

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

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {

    }

}