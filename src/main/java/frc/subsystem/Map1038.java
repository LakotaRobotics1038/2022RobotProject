package frc.subsystem;

import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.libraries.Limelight1038;
import frc.libraries.DriveTrain1038;
import frc.libraries.Gyro1038;
import java.lang.Math;

public class Map1038 implements Subsystem {
    private Limelight1038 limelight = Limelight1038.getInstance();
    private Gyro1038 gyro = Gyro1038.getInstance();
    private DriveTrain1038 drivetrain = DriveTrain1038.getInstance();

    private double gyroPos = gyro.getAngle();

    public double limelightZ = limelight.getYOffset();
    public double pos1X = 1;
    public double pos1Y = 1;
    final double hubHeight = 8.8; //8 feet 8 inches
    public double actualZ = hubHeight - limelightZ;

    private double encoderChange = 0;

    
    public void resetSensor() {
        gyro.reset();
        drivetrain.resetEncoders();
    }
    
    public void distanceChange() {
        double gyroPos = gyro.getAngle();
        double xChange = 0;
        double yChange = 0;
        if (0 <= gyroPos || gyroPos < 90) {
             xChange = encoderChange * Math.sin(gyroPos);
             yChange = -(encoderChange * Math.cos(gyroPos));
        }
        else if (90 <= gyroPos || gyroPos < 180) {
            xChange = -(encoderChange * Math.cos(gyroPos - 90));
            yChange = -(encoderChange * Math.sin(gyroPos - 90));
        }
        else if (180 <= gyroPos || gyroPos < 270) {
            xChange = -(encoderChange * Math.sin(gyroPos - 180));
            yChange = encoderChange * Math.cos(gyroPos - 180);
        }
        else {
            xChange = encoderChange * Math.cos(gyroPos - 270);
            yChange = encoderChange * Math.sin(gyroPos - 270);
        }

        pos1X += xChange;
        pos1Y += yChange;
    }

    public double turretAngle(double currentTurretPos) {
        gyroPos = gyro.getAngle();
        double targetAngle = Math.atan(pos1X/pos1Y) - gyroPos;
        return currentTurretPos - targetAngle; //turngle
    }

    public void resetCoords() {
        double a = limelight.getXOffset();
        double b = limelight.getYOffset();
        a *= pos1X/Math.abs(pos1X);
        b *= (pos1Y/Math.abs(pos1Y));
        pos1X = a;
        pos1Y = b;
    }
}
