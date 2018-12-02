package org.team1251.frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.team1251.frc.robot.humanInterface.input.DrivePower;
import org.team1251.frc.robot.humanInterface.input.HumanInput;
import org.team1251.frc.robot.subsystems.DriveTrain;

public class TeleopDrive extends Command {

    private final DriveTrain driveTrain;
    private final HumanInput humanInput;

    public TeleopDrive(DriveTrain driveTrain, HumanInput humanInput) {
        this.driveTrain = driveTrain;
        this.humanInput = humanInput;

        this.requires(driveTrain);
    }

    @Override
    protected void end() {
        driveTrain.drive(new DrivePower(0,0));
    }


    @Override
    protected void execute() {
        driveTrain.drive(humanInput.getDrivePower());
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
