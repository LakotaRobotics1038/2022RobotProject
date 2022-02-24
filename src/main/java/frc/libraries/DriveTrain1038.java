package frc.libraries;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;

import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import frc.libraries.TalonFX1038;

//Enum for each drive type
public class DriveTrain1038 implements Subsystem {

    private Accelerometer accelerometer = new BuiltInAccelerometer();
    private int roboWeight = 120;

    public enum DriveModes {
        tankDrive, singleArcadeDrive, dualArcadeDrive
    }

    // Setting the currentDriveMode to dualArcadeDrive
    public DriveModes currentDriveMode = DriveModes.dualArcadeDrive;

    // Ports for the motors
    public final double WHEEL_DIAMETER = 4;
    private final int HIGH_GEAR_PORT = 3;
    private final int LOW_GEAR_PORT = 2;
    private final static int RIGHT_FRONT_PORT = 0;
    private final static int RIGHT_BACK_PORT = 0;
    private final static int LEFT_FRONT_PORT = 0;
    private final static int LEFT_BACK_PORT = 0;

    // Wheel Motors
    final TalonFX1038 leftFrontTalon = new TalonFX1038(LEFT_FRONT_PORT);
    final TalonFX1038 rightFrontTalon = new TalonFX1038(RIGHT_FRONT_PORT);
    final TalonFX1038 leftBackTalon = new TalonFX1038(LEFT_BACK_PORT);
    final TalonFX1038 rightBackTalon = new TalonFX1038(RIGHT_BACK_PORT);

    // TODO: Look into pneumatic types
    // Creating a new DoubleSoleniod
    public DoubleSolenoid GearChangeSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, LOW_GEAR_PORT,
            HIGH_GEAR_PORT);

    // Setting isHighGear to false
    public boolean isHighGear = false;

    // Creating a new Differential Drive
    private DifferentialDrive differentialDrive = new DifferentialDrive(leftFrontTalon, rightFrontTalon);

    // Creating a new DriveTrain Instance
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
        return leftFrontTalon.getRotations() * Math.PI * WHEEL_DIAMETER;
    }

    // Get and return distance driven by the right of the robot in inches
    public double getRightDriveEncoderDistance() {
        return rightFrontTalon.getRotations() * Math.PI * WHEEL_DIAMETER;
    }

    public double getCANSparkRightEncoder() {
        return -leftFrontTalon.getPosition();
    }

    public double getCANSparkLeftEncoder() {
        return -rightFrontTalon.getSelectedSensorPosition();
    }

    // Pneumatics
    public void highGear() {
        // Doucment that the robot is in HighGear
        isHighGear = true;
        // Change the Gear Solenoid to Forward
        GearChangeSolenoid.set(DoubleSolenoid.Value.kForward);
    }

    public void lowGear() {
        // Document that the robot is in LowGear
        isHighGear = false;
        // Change the Gear Solenoid to Reverse
        GearChangeSolenoid.set(DoubleSolenoid.Value.kReverse);
    }

    public void resetEncoders() {
        // Reset the Motor encoders for driving
        leftFrontTalon.resetPosition();
        rightFrontTalon.resetPosition();
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

    /**
     * Returns the current robot speed in feet per second.
     * 
     * @return the current robot speed in feet per second.
     */
    public double roboSpeed() {
        // to get feet per second, mark a spot on the wheeel, rotate the wheel one
        // revolution, reset the encoder counts
        leftFrontTalon.getSelectedSensorVelocity();

        return accelerometer.getX();
    }
}