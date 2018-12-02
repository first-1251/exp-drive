package org.team1251.frc.robot.humanInterface.input;

public interface DriveInput {
    /**
     * Gets the currently requested drive power based on human input.
     */
    DrivePower getDrivePower(HumanInput humanInput);
}
