package frc.libraries;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;

public class TalonSRX1038 extends TalonSRX implements MotorController {
	public TalonSRX1038(int address) {
		super(address);
	}

	public void pidWrite(double output) {
		set(output);
	}

	@Override
	public void set(double speed) {
		super.set(super.getControlMode(), speed);
	}

	@Override
	public double get() {
		return super.getMotorOutputPercent();
	}

	@Override
	public void setInverted(boolean isInverted) {
		super.setInverted(isInverted);
	}

	@Override
	public boolean getInverted() {
		return super.getInverted();
	}

	@Override
	public void disable() {
		super.set(super.getControlMode(), 0);
	}

	@Override
	public void stopMotor() {
		super.set(super.getControlMode(), 0);
	}
}
