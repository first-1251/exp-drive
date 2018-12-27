package org.team1251.frc.robot.humanInterface.input;

import edu.wpi.first.wpilibj.Joystick;
import org.team1251.frc.robotCore.humanInterface.input.SimpleAnalogButtonConfig;
import org.team1251.frc.robotCore.humanInterface.input.SimpleStickConfig;
import org.team1251.frc.robotCore.humanInterface.input.gamepad.GamePad;
import org.team1251.frc.robotCore.humanInterface.input.gamepad.ModernGamePad;

/**
 * The HumanInput encapsulates everything related to human input and provides a clean interface for all commands and
 * subsystems to use.
 *
 * All knowledge about which buttons/sticks do what is contained within this class -- no other code should be reading
 * directly from the driver input devices. By centralizing this knowledge, it becomes much easier to adjust the control
 * scheme since it is not scattered throughout the code base. This also uses "information hiding" to make sure that
 * the rest of the robot does care about the details of how driver input is interpreted.
 */
public class HumanInput {

    /**
     * Indicates that command triggers have already been attached.
     */
    private boolean commandTriggersAttached = false;

    /**
     * The primary input device
     */
    private GamePad gamePad;

    /**
     * A helper which translates raw human input into drive input.
     */
    private DriveInput driveInput;

    /**
     * Creates a new instance
     */
    public HumanInput() {
        gamePad = new ModernGamePad(
                new Joystick(0),
                new SimpleStickConfig(.05, false, false),
                new SimpleStickConfig(.05, false, false),
                new SimpleAnalogButtonConfig(.05, .50),
                new SimpleAnalogButtonConfig(.05, .50)
        );
    }

    /**
     * Grants access to the game pad to other classes within this package.
     *
     * This is useful in cases when the logic for deriving specific input values is better handled by more specific
     * classes (for example, classes for deriving drive power in various ways).
     *
     * @return The GamePad which the human uses to control the robot.
     */
    GamePad getGamePad() {
        return gamePad;
    }

    /**
     * Sets the current means of interpreting drive input.
     */
    public void setDriveInput(DriveInput driveInput) {
        this.driveInput = driveInput;
    }

    public void attachCommandTriggers() {

        // TODO: Inject commands which need to be attached to command triggers.

        // This the typical way to prevent duplicate bindings.
        if (commandTriggersAttached) {
            return;
        }
        commandTriggersAttached = true;

        // TODO: Create the command triggers (Hint: org.team1251.frc.robotCore.humanInput.triggers.ButtonTrigger)
        // TODO: Attach commands to the command triggers.

        // By Default, there is no reason to "remember" the commands or the triggers as class properties. But now
        // would be a reasonable time to do it, if you have a reason to.
    }

    /**
     * Gets the currently requested drive power.
     */
    public DrivePower getDrivePower() {

        if (driveInput == null) {
            return new DrivePower(0,0);
        }

        return driveInput.getDrivePower(this);
    }

    public boolean isTestButtonPressed() {
        return gamePad.x().isPressed();
    }

    public boolean isTestResetButtonPressed() {
        return gamePad.b().isPressed();
    }
}