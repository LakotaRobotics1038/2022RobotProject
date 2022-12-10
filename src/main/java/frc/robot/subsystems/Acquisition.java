package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.Subsystem;

public class Acquisition implements Subsystem {
    // Ports and Constants
    private final int ACQUISITION_MOTOR_PORT = 9;
    private final int PUSH_OUT_ACQUISITION_CHANNEL = 2;
    private final int PULL_IN_ACQUISITION_CHANNEL = 3;
    private final double ACQUISITION_MOTOR_SPEED = 1.0;

    // Outputs
    private final CANSparkMax acquisitionMotor = new CANSparkMax(ACQUISITION_MOTOR_PORT, MotorType.kBrushless);
    private DoubleSolenoid acquisitionSolenoid = new DoubleSolenoid(PneumaticsModuleType.REVPH,
            PUSH_OUT_ACQUISITION_CHANNEL, PULL_IN_ACQUISITION_CHANNEL);

    // Enums
    public enum AcquisitionStates {
        In, Out
    }

    // States
    public AcquisitionStates acquisitionState = AcquisitionStates.In;

    // Singleton Setup
    private static Acquisition instance;

    public static Acquisition getInstance() {
        if (instance == null) {
            System.out.println("Creating a new Acquisition");
            instance = new Acquisition();
        }
        return instance;
    }

    private Acquisition() {
        acquisitionSolenoid.set(Value.kReverse);
    }

    /**
     * Sets the extension of acquisition
     */
    public void setAcqPos(AcquisitionStates state) {
        switch (state) {
            case In:
                acquisitionSolenoid.set(Value.kReverse);
                break;

            case Out:
                acquisitionSolenoid.set(Value.kForward);
                break;
        }

        acquisitionState = state;
    }

    /**
     * Runs acquisition to acquire balls
     */
    public void acquire() {
        acquisitionMotor.set(ACQUISITION_MOTOR_SPEED);
    }

    /**
     * Runs acquisition to spit balls out
     */
    public void dispose() {
        acquisitionMotor.set(-ACQUISITION_MOTOR_SPEED);
    }

    /**
     * Stop running the acquisition
     */
    public void stop() {
        acquisitionMotor.stopMotor();
    }
}
