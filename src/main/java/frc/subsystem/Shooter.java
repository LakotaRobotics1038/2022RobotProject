package frc.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Subsystem;

import frc.libraries.TalonSRX1038;
import frc.libraries.Limelight1038.LEDStates;
import frc.libraries.TalonFX1038;
import frc.libraries.Gyro1038;
import frc.libraries.Limelight1038;
import frc.subsystem.Storage.ManualStorageModes;

//Everything is based off distance and runs in a PID loop, no need for mapping or drivetrain.
//Best case we can implement math that will allow us to shoot and drive. PID might be able to do that with some extra math
public class Shooter implements Subsystem {
    private static Shooter shooter;
    private Storage storage = Storage.getInstance();
    // private Map1038 map = Map1038.getInstance(); Looks like we won't need drive
    // or map.
    // private DriveTrain1038 drive = DriveTrain1038.getInstance();
    private Limelight1038 limelight = Limelight1038.getInstance();
    private Gyro1038 gryo = Gyro1038.getInstance();
    private boolean isEnabled = false;
    private static double swivelSpeed = 0.2;
    private final static int LEFT_STOP = 684200; // TODO: Need to change both of these to represent where we have to
    // stop the turret.
    private final static int RIGHT_STOP = -684200;
    // Turret
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
    public TalonSRX1038 turretMotor = new TalonSRX1038(TURRET_MOTOR_PORT);

    // TODO: map angles to encoder counts, turret should go 160ish degrees both
    // ways.
    private final double hoodMaxDistance = 5.5; // inches
    private final double hoodMaxEncoder = 88;
    private final double encoderCountsPerInch = hoodMaxEncoder / hoodMaxDistance;
    private double hoodSetpoint = 0;
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
    // private final double speedSetpoint = limelight.getShooterSetpoint();
    private final double speedTolerance = 1;
    private final static double speedP = 0.00007;
    private final static double speedI = 0.0;
    private final static double speedD = 0.0;
    private PIDController speedPID = new PIDController(speedP, speedI, speedD);
    private boolean isRunning = false;

    // Motor speed for shooter feeder
    private final static double feedSpeed = 1;

    private Shooter() {
        shooterMotor1.setInverted(false);
        shooterMotor2.follow(shooterMotor1);
        shooterMotor2.setInverted(InvertType.OpposeMaster);
        compressionMotor.setInverted(false);
        turretPID.setSetpoint(positionSetpoint);
        turretPID.setTolerance(positionTolerance);
        turretPID.disableContinuousInput();
        turretMotor.setSelectedSensorPosition(0);

        // speedPID.setSetpoint(speedSetpoint);
        speedPID.setTolerance(speedTolerance);
        speedPID.enableContinuousInput(-1, 1);

        hoodPID.setSetpoint(limelight.getTargetDistance()); // TODO: Figure out what to divide by.
        hoodPID.setTolerance(hoodTolerance);
        hoodPID.disableContinuousInput();
        hoodMotor.setInverted(true);
        hoodMotor.getEncoder().setPositionConversionFactor(1 / encoderCountsPerInch);
        hoodMotor.getEncoder().setPosition(0);

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
        // TODO: Fix this
        isEnabled = false;
        speedPID.calculate(0.0);
        shooterMotor1.set(0);
        compressionMotor.set(0);
        limelight.changeLEDStatus(LEDStates.Off);
    }

    /**
     * Zero's the hood
     *
     * @warning Does not reset encoder values; this only works if encoder has been
     *          set correctly.
     */
    public void zeroHood() {
        // TODO: move encoder to a variable up top
        if (hoodMotor.getEncoder().getPosition() > 0) {
            hoodMotor.set(-.5);
        } else {
            hoodMotor.set(0);
        }
    }

    /** Aims the hood */
    private void executeHoodPID() {
        double power = hoodPID.calculate(hoodMotor.getEncoder().getPosition()); // TODO:
        // fine tune this PID
        hoodMotor.set(power);
    }

    public void moveHoodManually(double speed) {
        hoodMotor.set(speed);
    }

    /**
     * aims turret towards target
     */
    private void executeAimPID() {
        // hoodSetpoint = limelight.getTargetDistance() / 60;
        double power = turretPID.calculate(limelight.getXOffset());
        if (turretMotor.getSelectedSensorPosition() > LEFT_STOP ||
                turretMotor.getSelectedSensorPosition() < RIGHT_STOP) {
            turretMotor.set(-power * 0.5);
        } else {
            turretMotor.set(power * 0.5);
        }
    }

    /**
     * sets the speed of the shooter
     */
    private void executeSpeedPID() {
        isRunning = true;
        speedPID.setSetpoint(limelight.getShooterSetpoint()); // limelight.getShooterSetpoint()
        double power = speedPID
                .calculate((shooterMotor1.getSelectedSensorVelocity() / 4100.00) + limelight.getMotorPower());
        // System.out.println("speed" + getShooterSpeed());
        // System.out.println("setpoint: " + speedPID.getSetpoint());
        // System.out.println("power" + power);
        compressionMotor.set(power);
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
            // executeHoodPID();
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
                turretMotor.set(swivelSpeed);
                break;
            case Right:
                turretMotor.set(-swivelSpeed);
                break;
        }
    }

    // get's the turret current encoder value
    /**
     * @return The current direction in degrees of the turret.
     */
    public double getTurretEncoder() {
        return turretMotor.getSelectedSensorPosition();// * 180.00 / 82000.00; // converts radians to degrees
    }

    // gets the current shooter speed
    /**
     * This returns the current shooterSpeed.
     *
     * @return The current shooter speed.
     */
    public double getShooterSpeed() {
        return shooterMotor1.getSelectedSensorVelocity() / 4100.00; // converts to speed
    }

    // this sets the turret encoder position to 0
    /**
     * This resets the encoder value back to 0 for the turret encoder.
     */
    public void resetTurretEncoder() {
        turretMotor.setSelectedSensorPosition(0);
    }

    // stops the turret from moving
    /**
     * Stops the turret from moving.
     */
    private void stopTurret() {
        turretMotor.set(0);
    }

    // returns the turret directions
    /**
     * Returns the current direction the turret is moving.
     *
     * @return The turret Direction.
     */
    public TurretDirections getTurretDirection() {
        return currentTurretDirection;
    }

    // code red mountain dew TODO: change this name to code red
    /** This was goToCrashPosition. This has been renamed to codeRed */
    public void returnToZero() {
        zeroHood();
        if (Math.abs(turretMotor.getSelectedSensorPosition()) < 8000) {
            stopTurret();
        } else if (turretMotor.getSelectedSensorPosition() > 0) {
            currentTurretDirection = TurretDirections.Right;
            moveTurret();
        } else if (turretMotor.getSelectedSensorPosition() < 0) {
            currentTurretDirection = TurretDirections.Left;
            moveTurret();
        }
    }

    // moves the turret
    /**
     * This code used to move the turret via a position PID, use
     * turnTurret now.
     */

    public void findTarget() {
        limelight.changeLEDStatus(LEDStates.On);
        // System.out.println("Can see target? " + limelight.canSeeTarget());
        if (turretMotor.getSelectedSensorPosition() <= RIGHT_STOP) {
            currentTurretDirection = TurretDirections.Left;
            moveTurret();
        } else if (turretMotor.getSelectedSensorPosition() >= LEFT_STOP) {
            currentTurretDirection = TurretDirections.Right;
            moveTurret();
        } else if (limelight.canSeeTarget()) {
            executeAimPID();
            // moveTurret();
        } else {
            moveTurret();
        }
    }

    /**
     *
     * @return Hood encoder count in inches.
     */
    public double getHoodEncoder() {
        return hoodMotor.getEncoder().getPosition();
    }
}