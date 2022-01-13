package frc.libraries;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;

import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.DoubleSolenoid;

import frc.libraries.TalonSRX1038;

public class DriveTrain1038 implements Subsystem {
    public enum DriveModes {
        tankDrive, singleArcadeDrive, dualArcadeDrive
    }


    public DriveModes currentDriveMode = DriveModes.dualArcadeDrive;

    //Change these numbers for each new robot       v
    public final double WHEEL_DIAMETER = 4;
    private final int HIGH_GEAR_PORT = 3;
    private final int LOW_GEAR_PORT = 2;
    private final static int RIGHT_FRONT_PORT = 0;
    private final static int RIGHT_BACK_PORT = 0;
    private final static int LEFT_FRONT_PORT = 0;
    private final static int LEFT_BACK_PORT = 0;
    //Change these numbers for each new robot       ^

    
    final TalonSRX1038 leftFrontTalon = new TalonSRX1038(LEFT_FRONT_PORT);
    final TalonSRX1038 rightFrontTalon = new TalonSRX1038(RIGHT_FRONT_PORT);
    final TalonSRX1038 leftBackTalon = new TalonSRX1038(LEFT_BACK_PORT); 
    final TalonSRX1038 rightBackTalon = new TalonSRX1038(RIGHT_BACK_PORT); 


    public DoubleSolenoid GearChangeSolenoid = new DoubleSolenoid(LOW_GEAR_PORT, HIGH_GEAR_PORT);
    public boolean isHighGear = false;

    

    private DifferentialDrive differentialDrive;
    private static DriveTrain1038 driveTrain;

    public static DriveTrain1038 getInstance() {
        if (driveTrain == null) {
            System.out.println("Creating a new DriveTrain");
            driveTrain = new DriveTrain1038();
        }
        return driveTrain;
    }

    public DriveTrain1038() {
    }

    // Get and return distance driven by the left of the robot in inches
    public double getLeftDriveEncoderDistance() {
        return leftFrontTalon.getSelectedSensorPosition() * Math.PI * WHEEL_DIAMETER;
    }

    // Get and return distance driven by the right of the robot in inches
    public double getRightDriveEncoderDistance() {
        return rightFrontTalon.getSelectedSensorPosition() * Math.PI * WHEEL_DIAMETER;
    }

    public double getCANSparkRightEncoder() {
        return -leftFrontTalon.getSelectedSensorPosition();
    }

    public double getCANSparkLeftEncoder() {
        return -rightFrontTalon.getSelectedSensorPosition();
    }

    // Pneumatics
    public void highGear() {
        isHighGear = true;
        GearChangeSolenoid.set(DoubleSolenoid.Value.kForward);
    }

    public void lowGear() {
        isHighGear = false;
        GearChangeSolenoid.set(DoubleSolenoid.Value.kReverse);
    }

    public void resetEncoders() {
        leftFrontTalon.setSelectedSensorPosition(0);
        rightFrontTalon.setSelectedSensorPosition(0);
    }

    // Switch between drive modes
    public void driveModeToggler() {
        switch (currentDriveMode) {
        case tankDrive:
            currentDriveMode = DriveModes.singleArcadeDrive;
            break;
        case singleArcadeDrive:
            currentDriveMode = DriveModes.dualArcadeDrive;
            break;
        case dualArcadeDrive:
            currentDriveMode = DriveModes.tankDrive;
            break;
        default:
            System.out.println("Help I have fallen and I can't get up!");
            break;
        }
    }

    // Drive robot with tank controls (input range -1 to 1 for each stick)
    public void tankDrive(double leftStickInput, double rightStickInput) {
        differentialDrive.tankDrive(leftStickInput, rightStickInput, true);
    }

    // Drive robot using a single stick (input range -1 to 1)
    public void singleAracadeDrive(double speed, double turnValue) {
        differentialDrive.arcadeDrive(speed, turnValue, true);
    }

    // Drive robot using 2 sticks (input ranges -1 to 1)
    public void dualArcadeDrive(double yaxis, double xaxis) {
        differentialDrive.arcadeDrive(yaxis, xaxis, true);
    }
}