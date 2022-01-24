package frc.libraries;

import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;

public class TalonFX1038 extends TalonFX implements MotorController {
	public TalonFX1038(int address) {
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

	public double getPosition() {
		return super.getSelectedSensorPosition();
	}

	public void setPosition(int i) {
		super.setSelectedSensorPosition(i);
	}

	public void resetPosition() {
		setPosition(0);
	}

	public double getRotations() {
		return getPosition() / 360;
	}
}
