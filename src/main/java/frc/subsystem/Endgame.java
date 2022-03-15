package frc.subsystem;

import frc.libraries.DigitalInput1038;
import frc.libraries.TalonFX1038;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class Endgame {

    // Ports and Constants
    private final int ELEVATOR_PORT = 62;
    private final int ROTATOR_PORT = 1;
    private final int RATCHET_ON_PORT = 4;
    private final int RATCHET_OFF_PORT = 5;
    private boolean locked = true;
    private int endgameTop = 230; // TODO: change encoder counts to correct value
    private int endgameBottom = 0;
    private int LIMIT_SWITCH_PORT = 3;
    // This is from the right side of the robot looking at it with acq facing you.
    private int rotateLimitLeft = 150; // TODO: replace this with the actually encoder counts
    private int rotateLimitRight = 0;

    // Inputs and Outputs
    private final DigitalInput1038 bottomLimit = new DigitalInput1038(LIMIT_SWITCH_PORT);
    private final TalonFX1038 rotator = new TalonFX1038(ROTATOR_PORT);
    private final TalonFX1038 elevator = new TalonFX1038(ELEVATOR_PORT);
    private final DoubleSolenoid ratchetSolenoid = new DoubleSolenoid(PneumaticsModuleType.REVPH, RATCHET_ON_PORT,
            RATCHET_OFF_PORT);
    public static Endgame endgame;

    public static Endgame getInstance() {
        if (endgame == null) {
            System.out.println("Creating a new Dashbard");
            endgame = new Endgame();
        }
        return endgame;
    }

    private Endgame() {
    }

    // Encoders for the Motors
    public double getElevatorEncoderPosition() {
        return elevator.getPosition();
    }

    public double getRotatorEncodePosition() {
        return rotator.getPosition(); // might need some extra weird math
    }

    public void releaseRatchet() {
        if (locked) {
            ratchetSolenoid.set(Value.kForward);
            locked = false;
        }
    }

    public void engageRatchet() {
        if (!locked) {
            ratchetSolenoid.set(Value.kReverse);
            locked = true;
        }
    }

    public void liftElevator() {
        releaseRatchet();
        if (!locked) {
            elevator.set(ControlMode.Position, endgameTop);
            engageRatchet();
        }
    }

    public void lowerElevator() {
        if (!bottomLimit.get()) {
            elevator.set(ControlMode.Position, endgameBottom);
        } else {
            System.out.println("You are at the bottom limit already.");
        }
    }

    public void rotateRight() {
        rotator.setNeutralMode(NeutralMode.Brake);
        rotator.set(ControlMode.Position, rotateLimitRight);
    }

    public void rotateLeft() {
        rotator.setNeutralMode(NeutralMode.Brake);
        rotator.set(ControlMode.Position, rotateLimitLeft);
    }

}
