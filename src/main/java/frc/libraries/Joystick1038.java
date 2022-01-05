package frc.libraries;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;


public class Joystick1038 extends Joystick {
    // Button Locations
	private final int X_BUTTON = 3;
	private final int A_BUTTON = 1;
	private final int B_BUTTON = 2;
	private final int Y_BUTTON = 4;
	private final int LEFT_BUTTON = 5;
	private final int RIGHT_BUTTON = 6;
	private final int SQUARE_BUTTON = 7;
	private final int LINE_BUTTON = 8;
	private final int LEFT_JOYSTICK_CLICK = 9;
	private final int RIGHT_JOYSTICK_CLICK = 10;

	// Joystick locations
	private final int LEFT_STICK_HORIZONTAL = 0;
	private final int LEFT_STICK_VERTICAL = 1;
	private final int RIGHT_STICK_HORIZONTAL = 4;
	private final int RIGHT_STICK_VERTICAL = 5;
	private final int LEFT_TRIGGER = 2;
	private final int RIGHT_TRIGGER = 3;

    /**
	 * Creates a new Xbox joystick object
	 * @param port USB port the joystick should be in 
	 */
    public Joystick1038(int port) {
        super(port);
	}
	
	/**
	 * Returns the state of the POV on the controller
	 * 
	 * @return value of POV
	 */
	public int getPOV() {
		return getPOV();
	}

    /**
	 * Returns the state of the X button on the controller
	 * 
	 * @return is the X button pressed
	 */
    public boolean getXButton() {
        return getRawButton(X_BUTTON);
    }

    /**
	 * Returns the state of the A button on the controller
	 * 
	 * @return is the A button pressed
	 */
	public boolean getAButton() {
		return getRawButton(A_BUTTON);
	}

	/**
	 * Returns the state of the B button on the controller
	 * 
	 * @return is the B button pressed
	 */
	public boolean getBButton() {
		return getRawButton(B_BUTTON);
	}

	/**
	 * Returns the state of the Y button on the controller
	 * 
	 * @return is the Y button pressed
	 */
	public boolean getYButton() {
		return getRawButton(Y_BUTTON);
	}

	/**
	 * Returns the state of the left button on the controller
	 * 
	 * @return is the left button pressed
	 */
	public boolean getLeftButton() {
		return getRawButton(LEFT_BUTTON);
	}

	/**
	 * Returns the state of the right button on the controller
	 * 
	 * @return is the right button pressed
	 */
	public boolean getRightButton() {
		return getRawButton(RIGHT_BUTTON);
	}

	/**
	 * Returns the state of the back button on the controller
	 * 
	 * @return is the back button pressed
	 */
	public boolean getSquareButton() {
		return getRawButton(SQUARE_BUTTON);
	}

	/**
	 * Returns the state of the start button on the controller
	 * 
	 * @return is the start button pressed
	 */
	public boolean getLineButton() {
		return getRawButton(LINE_BUTTON);
	}

	/**
	 * Returns the state of the left joystick click on the controller
	 * 
	 * @return is the left joystick button pressed
	 */
	public boolean getLeftJoystickClick() {
		return getRawButton(LEFT_JOYSTICK_CLICK);
	}

	/**
	 * Returns the state of the right joystick click on the controller
	 * 
	 * @return is the right joystick button pressed
	 */
	public boolean getRightJoystickClick() {
		return getRawButton(RIGHT_JOYSTICK_CLICK);
	}
	
	/**
	 * Returns the joystick axis value or 0 if less than deadband
	 * 
	 * @return value of input axis, after deadband
	 */
	public double deadband(double value) {
		return Math.abs(value) < 0.10 ? 0 : value;
	}
    
    /**
	 * Returns the state of the left joystick on its vertical axis
	 * 
	 * @return value of the left joystick vertical axis, inverted so positive values
	 *         are joystick up
	 */
	public double getLeftJoystickVertical() {
		return deadband(-getRawAxis(LEFT_STICK_VERTICAL));
	}

	/**
	 * Returns the state of the left joystick on its horizontal axis
	 * 
	 * @return value of the left joystick horizontal axis
	 */
	public double getLeftJoystickHorizontal() {
		return deadband(getRawAxis(LEFT_STICK_HORIZONTAL));
	}

	/**
	 * Returns the state of the right joystick on its vertical axis
	 * 
	 * @return value of the right joystick vertical axis, inverted so positive
	 *         values are joystick up
	 */
	public double getRightJoystickVertical() {
		return deadband(-getRawAxis(RIGHT_STICK_VERTICAL));
	}

	/**
	 * Returns the state of the right joystick on its horizontal axis
	 * 
	 * @return value of the right joystick horizontal axis
	 */
	public double getRightJoystickHorizontal() {
		return deadband(getRawAxis(RIGHT_STICK_HORIZONTAL));
	}

    /**
	 * Returns the state of the left trigger on its axis
	 * 
	 * @return value of the left trigger axis
	 */
	public double getLeftTrigger() {
		return getRawAxis(LEFT_TRIGGER);
	}

	/**
	 * Returns the state of the right trigger on its axis
	 * 
	 * @return value of the right trigger axis
	 */
	public double getRightTrigger() {
		return getRawAxis(RIGHT_TRIGGER);
    }
	
	/**
	 * Returns the state of the left trigger as on or off
	 * 
	 * @return value of the left trigger axis
	 */
	public boolean getLeftTriggerDigital() {
		if(getRawAxis(LEFT_TRIGGER) > .5) {
			return true; 
		}
		else{
			return false;
		}
	}
	
	/**
	 * Returns the state of the right trigger as on or off
	 * 
	 * @return value of the right trigger axis
	 */
	public boolean getRightTriggerDigital() {
		if(getRawAxis(RIGHT_TRIGGER) > .5) {
			return true; 
		}
		else{
			return false;
		}
	}
	
    /**
	 * Sets the left rumble speed
	 * 
	 * @param speed the rumble speed between 0.0 and 1.0
	 * @return the new speed
	 */
	public double setLeftRumble(double speed) {
		setRumble(GenericHID.RumbleType.kLeftRumble, speed);
		return speed;
	}

	/**
	 * Sets the right rumble speed
	 * 
	 * @param speed the rumble speed between 0.0 and 1.0
	 * @return the new speed
	 */
	public double setRightRumble(double speed) {
		setRumble(GenericHID.RumbleType.kRightRumble, speed);
		return speed;
	}
}