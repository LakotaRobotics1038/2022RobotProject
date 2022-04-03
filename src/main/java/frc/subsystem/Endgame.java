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
    private final int ELEVATOR_PORT = 62;
    private final int ROTATOR_PORT = 1;
    private final int RATCHET_ON_PORT = 4;
    private final int RATCHET_OFF_PORT = 5;
    private final int ENDGAME_TOP = 160000;
    // TODO: replace this with the actually encoder counts
    private final int ROTATE_LIMIT_FORWARD = 40000;
    private final int ROTATE_LIMIT_BACKWARD = -1000;
    private final double RAISE_ELEVATOR_POWER = 0.4;
    private final double LOWER_ELEVATOR_POWER = -0.6;

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
        elevatorMotor.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector,
                LimitSwitchNormal.NormallyClosed, 0);
        rotatorMotor.resetPosition();
        rotatorMotor.setInverted(true);
        rotatorMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
        rotatorMotor.setNeutralMode(NeutralMode.Brake);
    }

    /**
     * Get the current position of the elevator arms
     *
     * @return position of the elevator arms in encoder counts
     */
    public double getElevatorEncoderPosition() {
        return elevatorMotor.getPosition();
    }

    /**
     * Get the current position of the rotator arms
     *
     * @return position of the rotator arms in encoder counts
     */
    public double getRotatorEncoderPosition() {
        return rotatorMotor.getPosition();
    }

    /**
     * Releases the ratchet the prevents the elevator arms from raising
     */
    public void releaseRatchet() {
        ratchetSolenoid.set(Value.kForward);
    }

    /**
     * Engages the ratchet the prevents the elevator arms from raising
     */
    public void engageRatchet() {
        ratchetSolenoid.set(Value.kReverse);
    }

    public boolean ratchetIsEngaged() {
        return ratchetSolenoid.get() == Value.kReverse;
    }

    /**
     * Raises the elevator arms after releasing the ratchet,
     * assuming the arms are not yet at maximum extension
     */
    public void liftElevator() {
        releaseRatchet();
        if (!ratchetIsEngaged() && elevatorMotor.getPosition() < ENDGAME_TOP) {
            elevatorMotor.set(RAISE_ELEVATOR_POWER);
        } else {
            engageRatchet();
            elevatorMotor.stopMotor();
        }
    }

    /**
     * Lowers the elevator until it contacts the lower limit switch
     * and engages the ratchet
     */
    public void lowerElevator() {
        engageRatchet();
        if (elevatorMotor.isRevLimitSwitchClosed() == 1) {
            elevatorMotor.set(LOWER_ELEVATOR_POWER);
        } else {
            elevatorMotor.stopMotor();
            elevatorMotor.resetPosition();
        }
    }

    /**
     * Stops the elevator motor at its current position
     */
    public void stopElevator() {
        elevatorMotor.stopMotor();
    }

    /**
     * Moves the rotator arms forward (away from the robot)
     */
    public void rotateForward() {
        if (rotatorMotor.getPosition() < ROTATE_LIMIT_FORWARD) {
            rotatorMotor.set(1);
        } else if (rotatorMotor.getPosition() >= ROTATE_LIMIT_FORWARD) {
            rotatorMotor.stopMotor();
        }
    }

    /**
     * Moves the rotator arms backward (toward the robot)
     */
    public void rotateBackward() {
        if (rotatorMotor.getPosition() > ROTATE_LIMIT_BACKWARD) {
            rotatorMotor.set(-1);
        } else if (rotatorMotor.getPosition() <= ROTATE_LIMIT_BACKWARD) {
            rotatorMotor.stopMotor();
        }
    }

    /**
     * Stops the rotator arms in their current location
     */
    public void stopRotator() {
        rotatorMotor.stopMotor();
    }
}
