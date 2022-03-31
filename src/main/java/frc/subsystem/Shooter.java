package frc.subsystem;

import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Subsystem;

import frc.libraries.TalonSRX1038;
import frc.libraries.Limelight1038.LEDStates;
import frc.libraries.TalonFX1038;
import frc.libraries.Limelight1038;
import frc.subsystem.Storage.ManualStorageModes;

public class Shooter implements Subsystem {
    private static Shooter shooter;
    private Storage storage = Storage.getInstance();
    private Limelight1038 limelight = Limelight1038.getInstance();
    private boolean overrideHoodPID = false;
    private boolean isEnabled = false;
    private static double SWIVEL_SPEED = 0.35;
    private final double TURRET_POWER_MULTIPLIER = 0.5;
    private final static int LEFT_STOP = 684000;
    private final static int RIGHT_STOP = -LEFT_STOP;
    private TurretDirections currentTurretDirection = TurretDirections.Left;

    public enum TurretDirections {
        Left, Right
    }

    public static Shooter getInstance() {
        if (shooter == null) {
            System.out.println("Creating a new Shooter");
            shooter = new Shooter();
        }
        return shooter;
    }

    // Ports and Constants
    private final int SHOOTER_MOTOR_PORT1 = 14;
    private final int SHOOTER_MOTOR_PORT2 = 13;
    private final int COMPRESSION_MOTOR_PORT = 18;
    private final int HOOD_MOTOR_PORT = 12;
    private final int TURRET_MOTOR_PORT = 19;

    // Inputs and Outputs
    public TalonFX1038 shooterMotor1 = new TalonFX1038(SHOOTER_MOTOR_PORT1);
    public TalonFX1038 shooterMotor2 = new TalonFX1038(SHOOTER_MOTOR_PORT2);
    public CANSparkMax compressionMotor = new CANSparkMax(COMPRESSION_MOTOR_PORT, MotorType.kBrushed);
    private CANSparkMax hoodMotor = new CANSparkMax(HOOD_MOTOR_PORT, MotorType.kBrushless);
    private RelativeEncoder hoodMotorEncoder = hoodMotor.getEncoder();
    public TalonSRX1038 turretMotor = new TalonSRX1038(TURRET_MOTOR_PORT);

    private final double hoodMaxDistance = 5.5; // inches
    private final double hoodMaxEncoder = 88;
    private final double encoderCountsPerInch = hoodMaxEncoder / hoodMaxDistance;
    private final double hoodTolerance = .25;
    private final static double hoodP = 0.65;
    private final static double hoodI = 0.03;
    private final static double hoodD = 0.0;
    private PIDController hoodPID = new PIDController(hoodP, hoodI, hoodD);

    private final double positionSetpoint = 0.0;
    private final double positionTolerance = 1;
    private final static double turretP = 0.08; // .15
    private final static double turretI = 0.0;
    private final static double turretD = 0.0;
    private PIDController turretPID = new PIDController(turretP, turretI, turretD);

    // Speed PID for shooter
    private final double speedTolerance = 1;
    private final static double speedP = 0.005;
    private final static double speedI = 0.0;
    private final static double speedD = 0.0;
    private PIDController speedPID = new PIDController(speedP, speedI, speedD);
    private boolean isRunning = false;
    public double speedMultiplier = 5.65;

    private Shooter() {

        shooterMotor1.setNeutralMode(NeutralMode.Coast);
        shooterMotor2.setNeutralMode(NeutralMode.Coast);
        compressionMotor.setIdleMode(IdleMode.kCoast);
        shooterMotor1.setInverted(false);
        shooterMotor2.follow(shooterMotor1);
        shooterMotor2.setInverted(InvertType.OpposeMaster);
        compressionMotor.setInverted(false);
        turretPID.setSetpoint(positionSetpoint);
        turretPID.setTolerance(positionTolerance);
        turretPID.disableContinuousInput();
        turretMotor.setSelectedSensorPosition(0);

        speedPID.setTolerance(speedTolerance);
        speedPID.disableContinuousInput();
        // TODO: Figure out what to divide by.
        hoodPID.setTolerance(hoodTolerance);
        hoodPID.disableContinuousInput();
        hoodMotor.setInverted(true);
        hoodMotorEncoder.setPositionConversionFactor(1 / encoderCountsPerInch);
        hoodMotorEncoder.setPosition(0);
    }

    /**
     * Feeds ball into shooter
     */
    public void feedBall() {
        if (isFinished()) {
            storage.setManualStorage(ManualStorageModes.In);
        }
    }

    /**
     * stops feeding balls into shooter
     */
    public void noFeedBall() {
        storage.disableManualStorage();
    }

    /**
     * disables speed motors and pid
     */
    public void disable() {
        isEnabled = false;
        speedPID.calculate(0.0);
        shooterMotor1.stopMotor();
        compressionMotor.stopMotor();
        limelight.changeLEDStatus(LEDStates.Off);
        shooter.zeroHood();
    }

    /**
     * Zero's the hood
     *
     * @warning Does not reset encoder values; this only works if encoder has been
     *          set correctly.
     */
    public void zeroHood() {
        if (hoodMotorEncoder.getPosition() > 0) {
            hoodMotor.set(-.5);
        } else {
            hoodMotor.stopMotor();
        }
    }

    /** Aims the hood */
    private void executeHoodPID() {
        double setPoint = MathUtil.clamp((limelight.getTargetDistance() / 40), 0, hoodMaxDistance);
        hoodPID.setSetpoint(setPoint);

        double power = hoodPID.calculate(hoodMotorEncoder.getPosition());
        hoodMotor.set(power);
    }

    public void moveHoodManually(double speed) {
        hoodMotor.set(speed);
    }

    /**
     * aims turret towards target
     */
    private void executeAimPID() {
        double power = turretPID.calculate(limelight.getXOffset());
        if (turretMotor.getPosition() >= LEFT_STOP) {
            turretMotor.set(-Math.abs(power * TURRET_POWER_MULTIPLIER));
        } else if (turretMotor.getPosition() <= RIGHT_STOP) {
            turretMotor.set(Math.abs(power * TURRET_POWER_MULTIPLIER));
        } else {
            turretMotor.set(power * TURRET_POWER_MULTIPLIER);
        }
    }

    private double getSpeedSetpoint() {
        Double distance = limelight.getTargetDistance();
        return distance * speedMultiplier;
    }

    /**
     * sets the speed of the shooter
     */
    private void executeSpeedPID() {
        isRunning = true;
        speedPID.setSetpoint(getSpeedSetpoint());

        double power = speedPID.calculate(getShooterSpeed() * 100);

        power = MathUtil.clamp(power, 0, 1);
        compressionMotor.set(power * .5);
        shooterMotor1.set(power);
    }

    // checks if the speedPID is at the setpoint (What speed we want the shooter at)
    /**
     *
     * @return if the speed is at it's setpoint.
     */

    public boolean speedOnTarget() {
        return speedPID.atSetpoint();
    }

    // sets the shooter to manual speed, disabling the PID
    /**
     * This is used to shoot manually.
     *
     *
     * @param speed the shooter should be at
     */
    public void shootManually(double speed) {
        shooterMotor1.set(speed);
        compressionMotor.set(speed);
    }

    // enables the PIDs and what not
    public void enable() {
        isEnabled = true;
    }

    // Executes the PID
    public void periodic() {
        if (isEnabled) {
            if (!overrideHoodPID) {
                executeHoodPID();
            }
            executeSpeedPID();
        }
    }

    /**
     * decides whether the robot is ready to shoot
     *
     * @return returns if robot is ready to shoot
     */
    public boolean isFinished() {
        return turretPID.atSetpoint() && speedPID.atSetpoint() && isRunning;
    }

    // Returns to see if the turret is aimed that the target
    public boolean turretOnTarget() {
        // return false;
        return turretPID.atSetpoint() && limelight.canSeeTarget();
    }

    // switch case for what direction the turret spins
    private void moveTurret() {
        switch (currentTurretDirection) {
            case Left:
                turretMotor.set(SWIVEL_SPEED);
                break;
            case Right:
                turretMotor.set(-SWIVEL_SPEED);
                break;
        }
    }

    /**
     * @return The current encoder counts of the turret.
     */
    public double getTurretEncoder() {
        return turretMotor.getPosition();
    }

    /**
     * This returns the current shooterSpeed.
     *
     * @return The current shooter speed.
     */
    public double getShooterSpeed() {
        return shooterMotor1.getSelectedSensorVelocity() / 2048;
    }

    /**
     * This resets the encoder value back to 0 for the turret encoder.
     */
    public void resetTurretEncoder() {
        turretMotor.setSelectedSensorPosition(0);
    }

    /**
     * Stops the turret from moving.
     */
    private void stopTurret() {
        turretMotor.stopMotor();
    }

    /**
     * Returns the current direction the turret is moving.
     *
     * @return The turret Direction.
     */
    public TurretDirections getTurretDirection() {
        return currentTurretDirection;
    }

    public void returnToZero() {
        zeroHood();
        if (Math.abs(turretMotor.getPosition()) < 50000) {
            stopTurret();
        } else if (turretMotor.getPosition() > 0) {
            currentTurretDirection = TurretDirections.Right;
            moveTurret();
        } else if (turretMotor.getPosition() < 0) {
            currentTurretDirection = TurretDirections.Left;
            moveTurret();
        }
    }

    /**
     * Uses limelight to find target
     */
    public void findTarget() {
        limelight.changeLEDStatus(LEDStates.On);
        // System.out.println("Can see target? " + limelight.canSeeTarget());
        if (turretMotor.getPosition() <= RIGHT_STOP) {
            currentTurretDirection = TurretDirections.Left;
            moveTurret();
        } else if (turretMotor.getPosition() >= LEFT_STOP) {
            currentTurretDirection = TurretDirections.Right;
            moveTurret();
        } else if (limelight.canSeeTarget()) {
            executeAimPID();
        } else {
            moveTurret();
        }
    }

    /**
     * @return Hood encoder count in inches.
     */
    public double getHoodEncoder() {
        return hoodMotorEncoder.getPosition();
    }

    /**
     * Stops the hoodPID while the shooter is enabled.
     */
    public void disableHoodPID() {
        overrideHoodPID = true;
        hoodMotor.stopMotor();
    }

    /**
     * Allows the hood to work while the shooter is enabled.
     */
    public void enableHoodPID() {
        overrideHoodPID = false;
    }
}