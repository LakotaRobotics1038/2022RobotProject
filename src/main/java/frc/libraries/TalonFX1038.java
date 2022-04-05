package frc.libraries;

import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;

public class TalonFX1038 extends TalonFX implements MotorController {
    public TalonFX1038(int address) {
        super(address);
        this.setStatusFramePeriod(StatusFrame.Status_4_AinTempVbat, 255);
        this.setStatusFramePeriod(StatusFrame.Status_6_Misc, 255);
        this.setStatusFramePeriod(StatusFrame.Status_9_MotProfBuffer, 255);
        this.setStatusFramePeriod(StatusFrame.Status_10_Targets, 255);
        this.setStatusFramePeriod(StatusFrame.Status_13_Base_PIDF0, 255);
        this.setStatusFramePeriod(StatusFrame.Status_14_Turn_PIDF1, 255);
        this.setStatusFramePeriod(StatusFrame.Status_15_FirmwareApiStatus, 255);
        this.setStatusFramePeriod(StatusFrame.Status_17_Targets1, 255);
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
}
