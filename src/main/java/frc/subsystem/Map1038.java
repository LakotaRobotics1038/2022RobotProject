package frc.subsystem;

import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.libraries.Limelight1038;
import frc.libraries.DriveTrain1038;
import frc.libraries.Gyro1038;
import java.lang.Math;

//TODO: NEED TO TALK WITH DREW ON ENCODER ISSUE. DISTANCE WILL BE SCREWED IF WE JUST USE ENCODER COUNTS

public class Map1038 implements Subsystem {
    private Limelight1038 limelight = Limelight1038.getInstance();
    private Gyro1038 gyro = Gyro1038.getInstance();
    private DriveTrain1038 drivetrain = DriveTrain1038.getInstance();

    private double gyroPos = gyro.getAngle();

    // public double limelightZ = limelight.getYOffset();
    final double HUB_HEIGHT = 104; // 8 feet 8 inches

    private double encoderChange = 0;

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

    public double turretAngle(double currentTurretPos) {
        return 2.0;
    }
}
