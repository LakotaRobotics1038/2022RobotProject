package frc.subsystem;

import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.libraries.Limelight1038;
import frc.libraries.DriveTrain1038;
import frc.libraries.Gyro1038;
import frc.subsystem.Shooter;
import java.lang.Math;

//TODO: NEED TO TALK WITH DREW ON ENCODER ISSUE. DISTANCE WILL BE SCREWED IF WE JUST USE ENCODER COUNTS

public class Map1038 implements Subsystem {
    private Limelight1038 limelight = Limelight1038.getInstance();
    private Gyro1038 gyro = Gyro1038.getInstance();
    private DriveTrain1038 drivetrain = DriveTrain1038.getInstance();
    private Shooter shooter = Shooter.getInstance();

    private double gyroPos = gyro.getAngle();
    private double distanceToHub = limelight.getYOffset();
    // private double speed = drivetrain.roboSpeed();
    private int firstCircle = 120;
    private int secondCircle = 240;
    private int thirdCircle = 360;

    private enum DistanceEnum {
        Close, Middle, Far, Error
    };
    // private double speed = 0.0;

    // public double limelightZ = limelight.getYOffset();
    final double HUB_HEIGHT = 104; // 8 feet 8 inches

    private static Map1038 map;

    public static Map1038 getInstance() {
        if (map == null) {
            System.out.println("creating a new map");
            map = new Map1038();
        }
        return map;
    }

    public void resetSensor() {
        gyro.reset();
        drivetrain.resetEncoders();
    }

    public double turretAngle() {
        return shooter.turretMotor.getRotations();
    }

    public DistanceEnum distanceDecision() {
        if (distanceToHub <= firstCircle) {
            return DistanceEnum.Close;
        } else if (distanceToHub > firstCircle && secondCircle >= distanceToHub) {
            return DistanceEnum.Middle;
        } else if (distanceToHub > secondCircle && thirdCircle >= distanceToHub) {
            return DistanceEnum.Far;
        }
        return DistanceEnum.Error;
    }
}
