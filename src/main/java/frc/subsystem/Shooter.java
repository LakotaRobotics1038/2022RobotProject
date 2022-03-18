package frc.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
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
    private final double hootSetpoint = 0.0;
    private final double hoodTolerance = 1;
    private final static double hoodP = 0.08;
    private final static double hoodI = 0.0;
    private final static double hoodD = 0.0;
    private PIDController hoodPID = new PIDController(hoodP, hoodI, hoodD);

    private final double positionSetpoint = 0.0;
    private final double positionTolerance = 1;
    private final static double TurrentP = 0.08; // .15
    private final static double TurrentI = 0.0;
    private final static double TurrentD = 0.0;
    private PIDController TurrentPID = new PIDController(TurrentP, TurrentI, TurrentD);

    // Speed PID for shooter
    // private final double speedSetpoint = limelight.getShooterSetpoint();
    private final double speedTolerance = 1000;
    private final static double speedP = 0.000007;
    private final static double speedI = 0.0;
    private final static double speedD = 0.0;
    private PIDController speedPID = new PIDController(speedP, speedI, speedD);
    private boolean isRunning = false;

    // Motor speed for shooter feeder
    private final static double feedSpeed = 1;

    private Shooter() {
        shooterMotor1.setInverted(true);
        shooterMotor2.setInverted(false);
        TurrentPID.setSetpoint(positionSetpoint);
        TurrentPID.setTolerance(positionTolerance);
        TurrentPID.disableContinuousInput();
        turretMotor.setSelectedSensorPosition(0);

        // speedPID.setSetpoint(speedSetpoint);
        speedPID.setTolerance(speedTolerance);
        speedPID.disableContinuousInput();

        hoodPID.setSetpoint(hootSetpoint);
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
    public void disablePID() {
        // TODO: Fix this
        speedPID.calculate(0.0);
        shooterMotor1.set(0);
        shooterMotor2.set(0);
    }

    /**
     * sets the position setpoint
     *
     * @deprecated DO NOT USE
     */
    @Deprecated
    public void initialize() {
        TurrentPID.setSetpoint(positionSetpoint);

    }

    /** Zero's the hood */
    public void zeroHood() {
        // TODO: move encoder to a variable up top
        if (hoodMotor.getEncoder().getPosition() > 0) {
            hoodMotor.set(-.5);
        } else {
            hoodMotor.set(0);
        }
    }

    /** Aims the hood */
    public void executeHoodPID() {
        double power = hoodPID.calculate(limelight.getTargetDistance()); // TODO:
        // fine tune this PID
        hoodMotor.set(power);
    }

    /**
     * aims turret towards target
     */
    public void executeAimPID() {
        System.out.println("PID");
        double power = TurrentPID.calculate(limelight.getXOffset());
        System.out.println("x " + limelight.getXOffset());
        if (turretMotor.getSelectedSensorPosition() > LEFT_STOP ||
                turretMotor.getSelectedSensorPosition() < RIGHT_STOP) {
            turretMotor.set(power * 0.5);
        } else {
            turretMotor.set(-power * 0.5);
        }
    }

    /**
     * sets the speed of the shooter
     */
    public void executeSpeedPID() {
        isRunning = true;
        speedPID.setSetpoint(limelight.getShooterSetpoint());
        double power = speedPID.calculate(shooterMotor1.getSelectedSensorVelocity())
                + limelight.getMotorPower();
        System.out.println("speed" + shooterMotor1.getSelectedSensorVelocity());
        System.out.println("setpoint: " + speedPID.getSetpoint());
        System.out.println("power" + power);
        shooterMotor1.set(power);
        shooterMotor2.set(power);
    }

    // Stops the speedPID
    public void disableSpeedPID() {
        isRunning = false;
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
        shooterMotor2.set(-speed);
    }

    // pass thru the turret direction you want, then this will set the turret to
    // that
    /**
     * @deprecated This shouldn't be used. Use turnTurret
     * @param value turret direction you want to move to.
     */
    @Deprecated
    public void setTurretDirection(TurretDirections value) {
        currentTurretDirection = value;
    }

    // enables the PIDs and what not
    public void enable() {
        isEnabled = true;
    }

    // Disabled the PIDs and what not, screw this
    public void disable() {
        isEnabled = false;
    }

    // Executes the PID
    public void periodic() {
        if (isEnabled) {
            // executeAimPID();
            executeHoodPID();
            executeSpeedPID();
        }
    }

    /**
     * stops and resets PID values if interrupted (potentially unnecessary)
     *
     * @param interrupted if the robot is interrupted
     */
    public void end(boolean interrupted) {
        if (interrupted) {
            System.out.println("position interrupted");
        }
        TurrentPID.reset();
        speedPID.reset();
    }

    /**
     * decides whether the robot is ready to shoot
     *
     * @return returns if robot is ready to shoot
     */
    public boolean isFinished() {
        return TurrentPID.atSetpoint() && speedPID.atSetpoint() && isRunning;
    }

    // Returns to see if the turret is aimed that the target
    public boolean turretOnTarget() {
        // return false;
        return TurrentPID.atSetpoint() && limelight.canSeeTarget();
    }

    // moves the turret
    public void turnTurret(double turnAngle) {
        // TODO: Do Math here for converting angle to encoder counts
        turretMotor.set(ControlMode.Position, turnAngle);
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

    // returns the hard stop of the robot before it breaks
    /**
     * @deprecated This method needs deleted
     *             public boolean getHardStop() {
     *             return hardStop.get();
     *             }
     */

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
        if (Math.abs(turretMotor.getSelectedSensorPosition()) < 4000) {
            stopTurret();
        } else if (turretMotor.getSelectedSensorPosition() > 0) {
            currentTurretDirection = TurretDirections.Right;
            moveTurret();
        } else if (turretMotor.getSelectedSensorPosition() < 0) {
            currentTurretDirection = TurretDirections.Left;
            moveTurret();
        }
    }

    // hold the turret position
    /**
     *
     * This code holds the turret at the current position.
     */
    public void holdPosition() {
        turretMotor.set(ControlMode.Velocity, 0);
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
            // executeAimPID();
            moveTurret();
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