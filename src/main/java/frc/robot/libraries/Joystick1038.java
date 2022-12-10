package frc.robot.libraries;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class Joystick1038 extends XboxController {
    // Buttons
    public final JoystickButton xButton;
    public final JoystickButton yButton;
    public final JoystickButton aButton;
    public final JoystickButton bButton;
    public final JoystickButton leftBumper;
    public final JoystickButton leftStick;
    public final Trigger leftTrigger;
    public final JoystickButton rightBumper;
    public final JoystickButton rightStick;
    public final Trigger rightTrigger;
    public final JoystickButton backButton;
    public final JoystickButton startButton;

    // Enums
    public enum PovPositions {
        Up, Down, Left, Right, None
    }

    /**
     * Creates a new Xbox joystick object
     *
     * @param port USB port the joystick should be in
     */
    public Joystick1038(int port) {
        super(port);
        xButton = new JoystickButton(this, XboxController.Button.kX.value);
        yButton = new JoystickButton(this, XboxController.Button.kY.value);
        aButton = new JoystickButton(this, XboxController.Button.kA.value);
        bButton = new JoystickButton(this, XboxController.Button.kB.value);
        leftBumper = new JoystickButton(this, XboxController.Button.kLeftBumper.value);
        leftStick = new JoystickButton(this, XboxController.Button.kLeftStick.value);
        leftTrigger = new Trigger(this::getLeftTriggerDigital);
        rightBumper = new JoystickButton(this, XboxController.Button.kRightBumper.value);
        rightStick = new JoystickButton(this, XboxController.Button.kRightStick.value);
        rightTrigger = new Trigger(this::getRightTriggerDigital);
        backButton = new JoystickButton(this, XboxController.Button.kBack.value);
        startButton = new JoystickButton(this, XboxController.Button.kStart.value);
    }

    /**
     * @deprecated
     *             Returns the state of the POV on the controller
     *
     * @return value of POV
     */
    @Deprecated
    public int getPOV() {
        throw new Error("Use getPOVPosition");
    }

    public PovPositions getPOVPosition() {
        int povVal = super.getPOV();
        switch (povVal) {
            case 0:
                return PovPositions.Up;
            case 90:
                return PovPositions.Right;
            case 180:
                return PovPositions.Down;
            case 270:
                return PovPositions.Left;
            default:
                return PovPositions.None;
        }
    }

    /**
     * Returns the joystick axis value or 0 if less than deadband
     *
     * @return value of input axis, after deadband
     */
    private double deadband(double value) {
        return Math.abs(value) < 0.10 ? 0 : value;
    }

    /**
     * Returns the state of the left joystick on its vertical axis
     *
     * @return value of the left joystick vertical axis, inverted so positive values
     *         are joystick up
     */
    public double getLeftY() {
        return deadband(-super.getLeftY());
    }

    /**
     * Returns the state of the left joystick on its horizontal axis
     *
     * @return value of the left joystick horizontal axis
     */
    public double getLeftX() {
        return deadband(super.getLeftX());
    }

    /**
     * Returns the state of the right joystick on its vertical axis
     *
     * @return value of the right joystick vertical axis, inverted so positive
     *         values are joystick up
     */
    public double getRightY() {
        return deadband(-super.getRightY());
    }

    /**
     * Returns the state of the right joystick on its horizontal axis
     *
     * @return value of the right joystick horizontal axis
     */
    public double getRightX() {
        return deadband(super.getRightX());
    }

    /**
     * Returns the state of the left trigger as on or off
     *
     * @return value of the left trigger axis
     */
    public boolean getLeftTriggerDigital() {
        return super.getLeftTriggerAxis() > .5;
    }

    /**
     * Returns the state of the right trigger as on or off
     *
     * @return value of the right trigger axis
     */
    public boolean getRightTriggerDigital() {
        return super.getRightTriggerAxis() > .5;
    }

    /**
     * Sets the left rumble speed
     *
     * @param speed the rumble speed between 0.0 and 1.0
     * @return the new speed
     */
    public void setLeftRumble(double speed) {
        setRumble(RumbleType.kLeftRumble, speed);
    }

    /**
     * Sets the right rumble speed
     *
     * @param speed the rumble speed between 0.0 and 1.0
     * @return the new speed
     */
    public void setRightRumble(double speed) {
        setRumble(RumbleType.kRightRumble, speed);
    }
}