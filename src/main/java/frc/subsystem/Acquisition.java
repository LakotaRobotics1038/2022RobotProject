package frc.subsystem;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.Subsystem;

public class Acquisition implements Subsystem {
    private static Acquisition acquisition;

    // Motor ports *CHANGE THESE OR ROBOT GETS ANGRY!
    // Ports and Constants
    private final int ACQUISITION_MOTOR_PORT = 9;
    private final int PUSH_OUT_ACQUISITION_CHANNEL = 2;
    private final int PULL_IN_ACQUISITION_CHANNEL = 3;
    private final double ACQUISITION_MOTOR_SPEED = 1.0;

    // States
    public AcquisitionStates acquisitionState = AcquisitionStates.In;
    public boolean XIsPressed = false;

    // Inputs and Outputs
    private final CANSparkMax acquisitionMotor = new CANSparkMax(ACQUISITION_MOTOR_PORT, MotorType.kBrushless);
    private DoubleSolenoid acquisitionSolenoid = new DoubleSolenoid(PneumaticsModuleType.REVPH,
            PUSH_OUT_ACQUISITION_CHANNEL, PULL_IN_ACQUISITION_CHANNEL);

    public enum AcquisitionStates {
        In, Out
    }

    public static Acquisition getInstance() {
        if (acquisition == null) {
            System.out.println("Creating a new Acquisition");
            acquisition = new Acquisition();
        }
        return acquisition;
    }

    private Acquisition() {
        acquisitionSolenoid.set(Value.kReverse);
    }

    // Motor Speeds

    public void toggleAcqPos() {
        switch (acquisitionState) {
            case In:
                acquisitionSolenoid.set(Value.kForward);
                acquisitionState = AcquisitionStates.Out;
                break;

            case Out:
                acquisitionSolenoid.set(Value.kReverse);
                acquisitionState = AcquisitionStates.In;
                break;
        }
    }

    public void runFwd() {
        // If motor is not moving
        acquisitionMotor.set(ACQUISITION_MOTOR_SPEED);
    }

    public void runRev() {
        acquisitionMotor.set(-ACQUISITION_MOTOR_SPEED);
    }

    public void stop() {
        acquisitionMotor.stopMotor();
    }
}
