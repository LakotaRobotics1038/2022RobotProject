package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDSubsystem;

import frc.robot.libraries.TalonFX1038;
import frc.robot.libraries.Limelight1038;

public class Shooter extends PIDSubsystem {
    private static Shooter instance;
    private Storage storage = Storage.getInstance();
    private Limelight1038 limelight = Limelight1038.getInstance();

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
    private final double TOLERANCE = 1;
    private final static double P = 0.005;
    private final static double I = 0.0;
    private final static double D = 0.0;
    public double speedMultiplier = 5.40;

    private Shooter() {
        super(new PIDController(P, I, D));
        getController().setTolerance(TOLERANCE);
        getController().disableContinuousInput();

        shooterMotor1.setNeutralMode(NeutralMode.Coast);
        shooterMotor2.setNeutralMode(NeutralMode.Coast);
        compressionMotor.setIdleMode(IdleMode.kCoast);
        shooterMotor1.setInverted(false);
        shooterMotor2.follow(shooterMotor1);
        shooterMotor2.setInverted(InvertType.OpposeMaster);
        compressionMotor.setInverted(false);
    }

    /**
     * Feeds ball into shooter
     */
    public void feedBall() {
        if (atSetpoint()) {
            storage.feedShooter(FEED_BALL_SPEED);
        }
    }

    /**
     * Get the setpoint for the speed PID
     *
     * @return the setpoint for the speed PID
     */
    @Override
    public double getSetpoint() {
        Double distance = limelight.getTargetDistance();
        return distance * speedMultiplier;
    }

    @Override
    protected void useOutput(double output, double setpoint) {
        double power = MathUtil.clamp(output, 0, 1);
        shootManually(power);
    }

    @Override
    protected double getMeasurement() {
        // TODO: why is this * 100 here?? - Wes
        return getShooterSpeed() * 100;
    }

    /**
     * Checks if the speedPID is at the setpoint (What speed we want the shooter at)
     *
     * @return if the speed is at it's setpoint.
     */
    public boolean atSetpoint() {
        return getController().atSetpoint();
    }

    /**
     * This is used to shoot manually.
     *
     * @param power motor power to give the shooter
     */
    public void shootManually(double power) {
        compressionMotor.set(power * 0.5);
        shooterMotor1.set(power);
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