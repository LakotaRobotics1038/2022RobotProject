package frc.subsystem;

import frc.libraries.TalonSRX1038;
import frc.libraries.Limelight1038;

public class Shooter implements Subsystem {

    private final int SHOOTER_MOTOR_PORT1 = 0;
    private final int SHOOTER_MOTOR_PORT2 = 0;
    private final int HOOD_MOTOR_PORT = 0;
    private final int TURRET_MOTOR_PORT = 0;
    private final int CONVERSION_NUM = 0;

    public TalonSRX1038 shooterMotor1 = new TalonSRX1038(shooterMotorPort1);
    public TalonSRX1038 shooterMotor2 = new TalonSRX1038(shooterMotorPort2);
    public TalonSRX1038 hoodMotor = new TalonSRX1038(hoodMotorPort);
    public TalonSRX1038 turretMotor = new TalonSRX1038(turretMotorPort);

    public void turretRotate() {

    }

}