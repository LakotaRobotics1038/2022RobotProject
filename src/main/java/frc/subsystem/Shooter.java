package frc.subsystem;

import frc.libraries.TalonSRX1038;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.libraries.Limelight1038;
import frc.libraries.TalonFX1038;

public class Shooter implements Subsystem {
    private static Shooter shooter;
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

    public TalonFX1038 shooterMotor1 = new TalonFX1038(SHOOTER_MOTOR_PORT1);
    public TalonFX1038 shooterMotor2 = new TalonFX1038(SHOOTER_MOTOR_PORT2);
    public TalonFX1038 hoodMotor = new TalonFX1038(HOOD_MOTOR_PORT);
    public TalonFX1038 turretMotor = new TalonFX1038(TURRET_MOTOR_PORT);

    public void turretRotate() {

    }
    public double getTurretEncoder() {
        return turretMotor.getPosition();
    }
    // public boolean getHardStop() {
    //     return hardStop.get();
    // }
}