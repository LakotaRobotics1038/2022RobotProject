package frc.subsystem;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

import frc.libraries.TalonFX1038;

public class Endgame {

    // Ports and Constants
    // Just nakey shafts? --Julian
    // That's just how I prefer it. --Kristyna
    private final int ELEVATOR_PORT = 62;
    private final int ROTATOR_PORT = 1;
    private final int RATCHET_ON_PORT = 4;
    private final int RATCHET_OFF_PORT = 5;
    public boolean locked = true;
    private final int ENDGAME_TOP = 160000;
    // This is from the right side of the robot looking at it with acq facing you.
    private int rotateLimitLeft = 40000; // TODO: replace this with the actually encoder counts
    private int rotateLimitRight = -1000;

    // Inputs and Outputs
    private final TalonFX1038 rotatorMotor = new TalonFX1038(ROTATOR_PORT);
    public final TalonFX1038 elevatorMotor = new TalonFX1038(ELEVATOR_PORT);
    private final DoubleSolenoid ratchetSolenoid = new DoubleSolenoid(
            PneumaticsModuleType.REVPH,
            RATCHET_ON_PORT,
            RATCHET_OFF_PORT);
    public static Endgame endgame;

    public static Endgame getInstance() {
        if (endgame == null) {
            System.out.println("Creating a new Dashboard");
            endgame = new Endgame();
        }
        return endgame;
    }

    private Endgame() {
        this.engageRatchet();
        elevatorMotor.setInverted(true);
        elevatorMotor.resetPosition();
        rotatorMotor.resetPosition();
        rotatorMotor.setInverted(true);
        rotatorMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
        elevatorMotor.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector,
                LimitSwitchNormal.NormallyClosed, 0);
    }

    // Encoders for the Motors
    public double getElevatorEncoderPosition() {
        return elevatorMotor.getPosition();
    }

    public double getRotatorMotorPosition() {
        return rotatorMotor.getPosition();
    }

    public double getRotatorEncoderPosition() {
        return rotatorMotor.getPosition(); // might need some extra weird math
    }

    public void releaseRatchet() {
        ratchetSolenoid.set(Value.kForward);
        locked = false;
    }

    public void engageRatchet() {
        ratchetSolenoid.set(Value.kReverse);
        locked = true;
    }

    public void liftElevator() {
        releaseRatchet();
        if (!locked && elevatorMotor.getPosition() < ENDGAME_TOP) {
            elevatorMotor.set(.4);
        } else {
            engageRatchet();
            elevatorMotor.stopMotor();
        }
    }

    public void lowerElevator() {
        engageRatchet();
        if (elevatorMotor.isRevLimitSwitchClosed() == 1) {
            elevatorMotor.set(-.8);
        } else if (elevatorMotor.isRevLimitSwitchClosed() == 0) {
            elevatorMotor.stopMotor();
            elevatorMotor.resetPosition();
        }
    }

    public void stopElevator() {
        elevatorMotor.stopMotor();
    }

    public void rotateLeft() {
        rotatorMotor.setNeutralMode(NeutralMode.Brake);
        if (rotatorMotor.getPosition() < rotateLimitLeft) {
            rotatorMotor.set(.1);
        } else if (rotatorMotor.getPosition() >= rotateLimitLeft) {
            rotatorMotor.stopMotor();
        }

    }

    public void rotateRight() {
        rotatorMotor.setNeutralMode(NeutralMode.Brake);
        if (rotatorMotor.getPosition() > rotateLimitRight) {
            rotatorMotor.set(-.1);
        } else if (rotatorMotor.getPosition() <= rotateLimitRight) {
            rotatorMotor.stopMotor();
        }
    }

    public void stopRotator() {
        rotatorMotor.stopMotor();
    }

}
