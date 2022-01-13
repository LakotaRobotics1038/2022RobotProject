package frc.libraries;

import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.libraries.Limelight1038;
import frc.libraries.DriveTrain1038;
import frc.libraries.Gyro1038;

public class Map1038 implements Subsystem {
    private Limelight1038 limelight = Limelight1038.getInstance();
    private Gyro1038 gyro = Gyro1038.getInstance();
    private DriveTrain1038 drivetrain = DriveTrain1038.getInstance();

    private double gryoPos = gyro.getAngle();

    public double limelightZ = limelight.getYOffset();
    public int pos1X = 1;
    public int pos1Y = 1;
    final double hubHeight = 8.8; //8 feet 8 inches
    public double actualZ = hubHeight - limelightZ;

    private double encoderChange = 0;

    
    public void resetSensor() {
        gyro.reset();
        drivetrain.resetEncoders();
    }
    
    public distanceChange() {
        gryoPos = gryo.getAngle;
        if (0 <= gryoPos || gryoPos < 90) {
             xChange = encoderChange * sin(gryoPos);
             yChange = -(encoderChange * cos(gryoPos));
        }
        else if (90 <= gryoPos || gryoPos < 180) {
            xChange = -(encoderChange * cos(gryoPos - 90));
            yChange = -(encoderChange * sin(gryoPos - 90));
        }
        else if (180 <= gryoPos || gryoPos < 270) {
            xChange = -(encoderChange * sin(gyroPos - 180));
            yChange = encoderChange * cos(gyroPos - 180);
        }
        else {
            xChange = encoderChange * cos(gyroPos - 270);
            yChange = encoderChange * sin(gyroPos - 270);
        }

        pos1X += xChange;
        pos1Y += yChange;
    }

    public void turretAngle(double currentTurretPos) {
        gyroPos = gyro.getAngle();
        targetAngle = atan(pos1X/pos1Y) - gyroPos;
        moveAngle = currentTurretPos - targetAngle; //turngle
    }

    public void resetCoords(double limelightAngle) {
        a *= pos1X/Math.abs(pos1X);
        b *= pos1Y/Math.abs(pos1Y);
        pos1X = a;
        pos1Y = b;
    }



}
