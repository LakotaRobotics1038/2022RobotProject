package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.libraries.TalonFX1038;

public class Elevator implements Subsystem {

    // Ports and Constants
    private final int ELEVATOR_MOTOR_PORT = 62;
    private final int RATCHET_ON_PORT = 4;
    private final int RATCHET_OFF_PORT = 5;
    private final int ELEVATOR_TOP = 160000;

    // Inputs and Outputs
    public final TalonFX1038 elevatorMotor = new TalonFX1038(ELEVATOR_MOTOR_PORT);
    private final DoubleSolenoid ratchetSolenoid = new DoubleSolenoid(
            PneumaticsModuleType.REVPH,
            RATCHET_ON_PORT,
            RATCHET_OFF_PORT);

    public static Elevator instance;

    public static Elevator getInstance() {
        if (instance == null) {
            System.out.println("Creating a new Dashboard");
            instance = new Elevator();
        }
        return instance;
    }

    private Elevator() {
        this.engageRatchet();
        elevatorMotor.setInverted(true);
        elevatorMotor.resetPosition();
        elevatorMotor.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector,
                LimitSwitchNormal.NormallyClosed, 0);
    }

    /**
     * Get the current position of the elevator arms
     *
     * @return position of the elevator arms in encoder counts
     */
    public double getPosition() {
        return elevatorMotor.getPosition();
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

    /**
     * Determine if the endgame ratchet is engaged
     *
     * @return true if ratchet is engaged
     */
    public boolean ratchetIsEngaged() {
        return ratchetSolenoid.get() == Value.kReverse;
    }

    /**
     * Sets the power to the elevator. Will override to stop if at encoder limit or
     * lower limit switch
     *
     * @param power positive moves up, negative moves down.
     */
    public void setPower(double power) {
        power = MathUtil.clamp(power, -1, 1);

        if ((power > 0 && !ratchetIsEngaged() && elevatorMotor.getPosition() < ELEVATOR_TOP) ||
                (power < 0 && !getLimitSwitch())) {
            elevatorMotor.set(power);
        } else {
            stop();
            if (getLimitSwitch()) {
                elevatorMotor.resetPosition();
            }
        }
    }

    /**
     * Gets the value of the elevator limit switch
     *
     * @return true if elevator is at bottom
     */
    private boolean getLimitSwitch() {
        return elevatorMotor.isRevLimitSwitchClosed() == 0;
    }

    /**
     * Stops the elevator motor at its current position
     */
    public void stop() {
        elevatorMotor.stopMotor();
    }
}
