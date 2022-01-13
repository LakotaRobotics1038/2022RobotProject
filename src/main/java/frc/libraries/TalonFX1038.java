package frc.libraries;

import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj.SpeedController;

public class TalonFX1038 extends TalonFX implements SpeedController {
    public TalonFX1038 (int address) {
		super(address);
	}
	
	@Override
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
