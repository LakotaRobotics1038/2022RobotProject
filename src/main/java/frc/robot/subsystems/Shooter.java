package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Subsystem;

import frc.robot.libraries.Limelight1038.LEDStates;
import frc.robot.libraries.TalonFX1038;
import frc.robot.libraries.Limelight1038;

public class Shooter implements Subsystem {
    private static Shooter instance;
    private Storage storage = Storage.getInstance();
    private Limelight1038 limelight = Limelight1038.getInstance();
    private boolean isEnabled = false;

    public static Shooter getInstance() {
        if (instance == null) {
            System.out.println("Creating a new Shooter");
            instance = new Shooter();
        }
        return instance;
    }

    // Ports and Constants
    private final int SHOOTER_MOTOR_PORT1 = 14;
    private final int SHOOTER_MOTOR_PORT2 = 13;
    private final int COMPRESSION_MOTOR_PORT = 18;
    private final double FEED_BALL_SPEED = 0.6;

    // Inputs and Outputs
    public TalonFX1038 shooterMotor1 = new TalonFX1038(SHOOTER_MOTOR_PORT1);
    public TalonFX1038 shooterMotor2 = new TalonFX1038(SHOOTER_MOTOR_PORT2);
    public CANSparkMax compressionMotor = new CANSparkMax(COMPRESSION_MOTOR_PORT, MotorType.kBrushed);

    // PID Controller Setup
    // Shooter Wheels
    private final double speedTolerance = 1;
    private final static double speedP = 0.005;
    private final static double speedI = 0.0;
    private final static double speedD = 0.0;
    private PIDController speedPID = new PIDController(speedP, speedI, speedD);
    public double speedMultiplier = 5.40;

    private Shooter() {
        shooterMotor1.setNeutralMode(NeutralMode.Coast);
        shooterMotor2.setNeutralMode(NeutralMode.Coast);
        compressionMotor.setIdleMode(IdleMode.kCoast);
        shooterMotor1.setInverted(false);
        shooterMotor2.follow(shooterMotor1);
        shooterMotor2.setInverted(InvertType.OpposeMaster);
        compressionMotor.setInverted(false);

        speedPID.setTolerance(speedTolerance);
        speedPID.disableContinuousInput();
    }

    /**
     * Feeds ball into shooter
     */
    public void feedBall() {
        if (isFinished()) {
            storage.feedShooter(FEED_BALL_SPEED);
        }
    }

    /**
     * Disables speed motors and pid
     */
    public void disable() {
        isEnabled = false;
        speedPID.reset();
        shooterMotor1.stopMotor();
        compressionMotor.stopMotor();
        limelight.changeLEDStatus(LEDStates.Off);
    }

    /**
     * Get the setpoint for the speed PID
     *
     * @return the setpoint for the speed PID
     */
    private double getSpeedSetpoint() {
        Double distance = limelight.getTargetDistance();
        return distance * speedMultiplier;
    }

    /**
     * Uses getSpeedSetpoint to get the shooter wheels moving at the appropriate
     * speed
     */
    private void executeSpeedPID() {
        speedPID.setSetpoint(getSpeedSetpoint());

        // TODO: why is this * 100 here?? - Wes
        double power = speedPID.calculate(getShooterSpeed() * 100);

        power = MathUtil.clamp(power, 0, 1);
        compressionMotor.set(power * .5);
        shooterMotor1.set(power);
    }

    /**
     * Checks if the speedPID is at the setpoint (What speed we want the shooter at)
     *
     * @return if the speed is at it's setpoint.
     */
    public boolean speedOnTarget() {
        return speedPID.atSetpoint();
    }

    /**
     * This is used to shoot manually.
     *
     * @param power motor power to give the shooter
     */
    public void shootManually(double power) {
        shooterMotor1.set(power);
        compressionMotor.set(power);
    }

    /**
     * Enables the shooter PIDs
     */
    public void enable() {
        isEnabled = true;
    }

    @Override
    public void periodic() {
        if (isEnabled) {
            executeSpeedPID();
        }
    }

    /**
     * Decides whether the shooter wheels are at speed
     *
     * @return are shooter wheels at speed
     */
    public boolean isFinished() {
        return speedPID.atSetpoint();
    }

    /**
     * This returns the current shooterSpeed.
     *
     * @return The current shooter speed.
     */
    public double getShooterSpeed() {
        return shooterMotor1.getSelectedSensorVelocity() / 2048;
    }
}