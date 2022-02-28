package frc.subsystem;

import frc.libraries.TalonSRX1038;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.libraries.Limelight1038;
import frc.libraries.TalonFX1038;
import frc.subsystem.Map1038;
import frc.libraries.DriveTrain1038;
import frc.subsystem.Storage;
import frc.subsystem.Storage.ManualStorageModes;

public class Shooter implements Subsystem {
  private static Shooter shooter;
  private Storage storage = Storage.getInstance();
  private Map1038 map = Map1038.getInstance();
  private DriveTrain1038 drive = DriveTrain1038.getInstance();
  private Limelight1038 limelight = Limelight1038.getInstance();
  private boolean isEnabled = false;
  private static double swivelSpeed = 0.2;
  private final static int LEFT_STOP = 0; // TODO: Need to change both of these to repersent where we have to stop the
                                          // turret.
  private final static int RIGHT_STOP = 0;
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

  private final int SHOOTER_MOTOR_PORT1 = 0;
  private final int SHOOTER_MOTOR_PORT2 = 0;
  private final int COMPRESSION_MOTOR_PORT = 0;
  private final int HOOD_MOTOR_PORT = 0;
  private final int TURRET_MOTOR_PORT = 0;
  private final int CONVERSION_NUM = 0;
  private double stationaryShooterSpeed = 0;
  private double shooterSpeed = 0;
  private double hoodAngle = 0;
  private double stationaryHoodAngle = 0;

  public TalonFX1038 shooterMotor1 = new TalonFX1038(SHOOTER_MOTOR_PORT1);
  public TalonFX1038 shooterMotor2 = new TalonFX1038(SHOOTER_MOTOR_PORT2);
  public TalonSRX1038 compressionMotor = new TalonSRX1038(COMPRESSION_MOTOR_PORT);
  public TalonFX1038 hoodMotor = new TalonFX1038(HOOD_MOTOR_PORT);
  public TalonFX1038 turretMotor = new TalonFX1038(TURRET_MOTOR_PORT);

  // TODO: map angles to encoder counts, turret should go 160 degrees both ways.

  private final double positionSetpoint = 0.0;
  private final double positionTolerance = 1;
  private final static double positionP = 0.08; // .15
  private final static double positionI = 0.0;
  private final static double positionD = 0.0;
  private PIDController positionPID = new PIDController(positionP, positionI, positionD);

  // Speed PID for shooter
  private final double speedSetpoint = limelight.getShooterSetpoint();
  private final double speedTolerance = 1000;
  private final static double speedP = 0.000007;
  private final static double speedI = 0.0;
  private final static double speedD = 0.0;
  private PIDController speedPID = new PIDController(speedP, speedI, speedD);
  private boolean isRunning = false;

  // Motor speed for shooter feeder
  private final static double feedSpeed = 1;

  private Shooter() {
    positionPID.setSetpoint(positionSetpoint);
    positionPID.setTolerance(positionTolerance);
    positionPID.disableContinuousInput();
    turretMotor.setSelectedSensorPosition(0);

    speedPID.setSetpoint(speedSetpoint);
    speedPID.setTolerance(speedTolerance);
    speedPID.disableContinuousInput();
  }

  /**
   * Feeds ball into shooter
   */
  public void feedBall() {
    if (isFinished()) {
      storage.enableManualStorage(ManualStorageModes.Forward);
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
   */
  public void initialize() {
    positionPID.setSetpoint(positionSetpoint);

  }

  /**
   * aims turret towards target
   */
  public void executeAimPID() {
    // System.out.println("PID");
    double power = positionPID.calculate(limelight.getXOffset());
    System.out.println("x " + limelight.getXOffset());
    turretMotor.set(power * 0.5);
  }

  /**
   * sets the speed of the shooter
   */
  public void executeSpeedPID() {
    isRunning = true;
    speedPID.setSetpoint(limelight.getShooterSetpoint());
    double power = speedPID.calculate(shooterMotor1.getSelectedSensorVelocity()) + limelight.getMotorPower();
    System.out.println("speed" + shooterMotor1.getSelectedSensorVelocity());
    System.out.println("setpoint: " + speedPID.getSetpoint());
    System.out.println("power" + power);
    shooterMotor1.set(-power);
    shooterMotor2.set(power);
  }

  // Stops the speedPID
  public void disableSpeedPID() {
    isRunning = false;
  }

  // checks if the speedPID is at the setpoint (What speed we want the shooter at)
  public boolean speedOnTarget() {
    return speedPID.atSetpoint();
  }

  // sets the shooter to manual speed, disabling the PID
  public void shootManually(double speed) {
    shooterMotor1.set(speed);
    shooterMotor2.set(-speed);
  }

  // pass thru the turret direction you want, then this will set the turret to
  // that
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
    positionPID.reset();
    speedPID.reset();
  }

  /**
   * decides whether the robot is ready to shoot
   * 
   * @return returns if robot is ready to shoot
   */
  public boolean isFinished() {
    return positionPID.atSetpoint() && speedPID.atSetpoint() && isRunning;
  }

  // Returns to see if the turret is aimed that the target
  public boolean turretOnTarget() {
    return positionPID.atSetpoint() && limelight.canSeeTarget();
  }

  /**
   * limits shooter turn radius
   */

  // moves the turret
  public void turnTurret(double turnSpeed) {
    turretMotor.set(turnSpeed);
  }

  // switch case for what direction the turret spins
  public void swivelEy() {
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
  public double getTurretEncoder() {
    return turretMotor.getSelectedSensorPosition() * 180.00 / 82000.00; // converts radians to degrees
  }

  // gets the current shooter speed
  public double getShooterSpeed() {
    return shooterMotor1.getSelectedSensorVelocity() / 4100.00; // converts to speed
  }

  // returns the hard stop of the robot before it breaks
  // public boolean getHardStop() {
  // return hardStop.get();
  // }

  // this sets the turret encoder position to 0
  public void resetTurretEncoder() {
    turretMotor.setSelectedSensorPosition(0);
  }

  // stops the turret from moving
  private void stopTurret() {
    turretMotor.set(0);
  }

  // returns the turret directions
  public TurretDirections getTurretDirection() {
    return currentTurretDirection;
  }

  // code red mountain dew TODO: change this name to code red
  public void goToCrashPosition() {
    if (Math.abs(turretMotor.getSelectedSensorPosition()) < 1000) {
      stopTurret();
    } else if (turretMotor.getSelectedSensorPosition() > 0) {
      currentTurretDirection = TurretDirections.Right;
      swivelEy();
    } else if (turretMotor.getSelectedSensorPosition() < 0) {
      currentTurretDirection = TurretDirections.Left;
      swivelEy();
    }
  }

  // hold the turret position
  public void holdPosition() {
    turretMotor.set(0);
  }

  // moves the turret
  public void move() {
    turretMotor.setSelectedSensorPosition(0);

    if (turretMotor.getSelectedSensorPosition() <= RIGHT_STOP) {
      currentTurretDirection = TurretDirections.Left;
      swivelEy();
    } else if (turretMotor.getSelectedSensorPosition() >= LEFT_STOP) {
      currentTurretDirection = TurretDirections.Right;
      swivelEy();
    } else if (limelight.canSeeTarget()) {
      executeAimPID();
    } else {
      swivelEy();
    }
  }
}