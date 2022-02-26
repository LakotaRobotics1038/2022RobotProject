package frc.subsystem;

import frc.libraries.TalonSRX1038;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.libraries.Limelight1038;
import frc.libraries.TalonFX1038;
import frc.subsystem.Map1038;
import frc.libraries.DriveTrain1038;

public class Shooter implements Subsystem {
  private static Shooter shooter;
  private Map1038 map = Map1038.getInstance();
  private DriveTrain1038 drive = DriveTrain1038.getInstance();

  public static Shooter getInstance() {
    if (shooter == null) {
      System.out.println("Creating a new Shooter");
      shooter = new Shooter();
    }
    return shooter;
  }

  private final int SHOOTER_MOTOR_PORT1 = 0;
  private final int SHOOTER_MOTOR_PORT2 = 0;
  private final int HOOD_MOTOR_PORT = 0;
  private final int TURRET_MOTOR_PORT = 0;
  private final int CONVERSION_NUM = 0;
  private double stationaryShooterSpeed = 0;
  private double shooterSpeed = 0;
  private double hoodAngle = 0;
  private double stationaryHoodAngle = 0;

  public TalonFX1038 shooterMotor1 = new TalonFX1038(SHOOTER_MOTOR_PORT1);
  public TalonFX1038 shooterMotor2 = new TalonFX1038(SHOOTER_MOTOR_PORT2);
  public TalonFX1038 hoodMotor = new TalonFX1038(HOOD_MOTOR_PORT);
  public TalonFX1038 turretMotor = new TalonFX1038(TURRET_MOTOR_PORT);

  // TODO: map angles to encoder counts, turret should go 160 degrees both ways.

  public void turretRotate() {

  }

  public double getTurretEncoder() {
    return turretMotor.getPosition();
  }

  public void shoot() {
    shooterSpeed = Math.sqrt((Math.pow(stationaryShooterSpeed, 2))
        + (2 * stationaryShooterSpeed * drive.roboSpeed()) * (Math.cos(map.turretAngle()))
        + (Math.pow(drive.roboSpeed(), 2)) * (Math.pow(Math.cos(map.turretAngle()), 2)));

    shooterMotor1.set(shooterSpeed);
    shooterMotor2.set(-shooterSpeed);

  }

  public void aim() {

  }

  public void moveHood() {
    hoodAngle = Math.atan(((stationaryShooterSpeed * Math.sin(stationaryHoodAngle))
        / (stationaryShooterSpeed * Math.cos(stationaryHoodAngle) + drive.roboSpeed() * Math.cos(map.turretAngle()))));

    // TODO: Convert hoodAngle to encoder counts
  }
  // public boolean getHardStop() {
  // return hardStop.get();
  // }
}