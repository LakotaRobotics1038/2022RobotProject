package frc.libraries;

import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

//Enum for each drive type
public class DriveTrain1038 implements Subsystem {

    private final int ROBOT_WEIGHT = 120;

    public enum DriveModes {
        tankDrive, singleArcadeDrive, dualArcadeDrive
    }

    public DriveModes currentDriveMode = DriveModes.dualArcadeDrive;
    public boolean isHighGear = false;

    // Ports for the motors
    public final double WHEEL_DIAMETER = 4;
    private final int HIGH_GEAR_PORT = 1;
    private final int LOW_GEAR_PORT = 0;
    private final static int RIGHT_FRONT_PORT = 6;
    private final static int RIGHT_BACK_PORT = 7;
    private final static int LEFT_FRONT_PORT = 4;
    private final static int LEFT_BACK_PORT = 5;

    // Wheel Motors
    final TalonFX1038 leftFrontTalon = new TalonFX1038(LEFT_FRONT_PORT);
    final TalonFX1038 rightFrontTalon = new TalonFX1038(RIGHT_FRONT_PORT);
    final TalonFX1038 leftBackTalon = new TalonFX1038(LEFT_BACK_PORT);
    final TalonFX1038 rightBackTalon = new TalonFX1038(RIGHT_BACK_PORT);
    private Accelerometer accelerometer = new BuiltInAccelerometer();

    public DoubleSolenoid GearChangeSolenoid = new DoubleSolenoid(
            PneumaticsModuleType.REVPH,
            LOW_GEAR_PORT,
            HIGH_GEAR_PORT);

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
        leftBackTalon.follow(leftFrontTalon);
        rightBackTalon.follow(rightFrontTalon);
        differentialDrive = new DifferentialDrive(leftFrontTalon, rightFrontTalon);
    }

    /**
     * Get and return distance driven by the left of the robot in inches
     */
    public double getLeftDriveEncoderDistance() {
        return leftFrontTalon.getRotations() * Math.PI * WHEEL_DIAMETER;
    }

    /**
     * Get and return distance driven by the right of the robot in inches
     */
    public double getRightDriveEncoderDistance() {
        return rightFrontTalon.getRotations() * Math.PI * WHEEL_DIAMETER;
    }

    /**
     * Set the drive train to use high gear
     */
    public void highGear() {
        isHighGear = true;
        GearChangeSolenoid.set(Value.kForward);
    }

    /**
     * Set the drive train to use low gear
     */
    public void lowGear() {
        isHighGear = false;
        GearChangeSolenoid.set(Value.kReverse);
    }

    /**
     * Reset drive encoders to zero
     */
    public void resetEncoders() {
        leftFrontTalon.resetPosition();
        rightFrontTalon.resetPosition();
    }

    /**
     * Toggle drive mode between tank, single arcade, and dual arcade
     */
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

    /**
     * Drive the robot in tank mode
     *
     * @param leftStickInput  the forward speed of the left side of the robot
     *                        (-1 to 1)
     * @param rightStickInput the forward speed of the right side of the robot
     *                        (-1 to 1)
     */
    public void tankDrive(double leftStickInput, double rightStickInput) {
        differentialDrive.tankDrive(leftStickInput, rightStickInput, true);
    }

    /**
     * Drive the robot in single stick arcade mode
     *
     * @param speed     The forward speed of the robot (-1 to 1)
     * @param turnValue The turn speed of the robot (-1 to 1)
     */
    public void arcadeDrive(double speed, double turnValue) {
        differentialDrive.arcadeDrive(speed, turnValue, true);
    }

    /**
     * Returns the current robot speed in feet per second.
     *
     * @return the current robot speed in feet per second.
     */
    public double robotSpeed() {
        return accelerometer.getX();
    }
}