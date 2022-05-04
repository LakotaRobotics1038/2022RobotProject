package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.libraries.TalonFX1038;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;

import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

//Enum for each drive type
public class DriveTrain implements Subsystem {

    public enum DriveModes {
        tankDrive, singleArcadeDrive, dualArcadeDrive
    }

    public DriveModes currentDriveMode = DriveModes.dualArcadeDrive;
    public boolean isHighGear = false;

    // Ports for the motors
    private final double WHEEL_DIAMETER = 6;
    private final double TALON_COUNTS_PER_REVOLUTION = 2048;
    private final double GEAR_RATIO = 12.3;
    private final double ENCODER_COUNTS_PER_REVOLUTION = GEAR_RATIO * TALON_COUNTS_PER_REVOLUTION;
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
    private static DriveTrain driveTrain;

    public static DriveTrain getInstance() {
        if (driveTrain == null) {
            System.out.println("Creating a new DriveTrain");
            driveTrain = new DriveTrain();
        }
        return driveTrain;
    }

    private DriveTrain() {
        leftFrontTalon.setInverted(true);
        leftBackTalon.setInverted(InvertType.FollowMaster);
        rightFrontTalon.setInverted(false);
        rightBackTalon.setInverted(InvertType.FollowMaster);
        leftBackTalon.follow(leftFrontTalon);
        rightBackTalon.follow(rightFrontTalon);
        differentialDrive = new DifferentialDrive(leftFrontTalon, rightFrontTalon);
        this.lowGear();
        this.setCoastMode();
    }

    /**
     * Get and return distance driven by the left of the robot in inches
     */
    public double getLeftDriveEncoderDistance() {
        return leftFrontTalon.getPosition() / ENCODER_COUNTS_PER_REVOLUTION * Math.PI * WHEEL_DIAMETER;
    }

    /**
     * Get and return distance driven by the right of the robot in inches
     */
    public double getRightDriveEncoderDistance() {
        return rightFrontTalon.getPosition() / ENCODER_COUNTS_PER_REVOLUTION * Math.PI * WHEEL_DIAMETER;
    }

    /**
     * Set the drive train to use high gear
     */
    public void highGear() {
        isHighGear = true;
        GearChangeSolenoid.set(Value.kReverse);
    }

    /**
     * Set the drive train to use low gear
     */
    public void lowGear() {
        isHighGear = false;
        GearChangeSolenoid.set(Value.kForward);
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
    public void toggleDriveMode() {
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

    /**
     * Set the drive train to brake mode. You should do this in auton init
     */
    public void setBrakeMode() {
        this.setNeutralMode(NeutralMode.Brake);
    }

    /**
     * Set the drive train to coast. Typically used in teleop init
     */
    public void setCoastMode() {
        this.setNeutralMode(NeutralMode.Coast);
    }

    /**
     * Helper function to set all of the drive controllers to the same neutral mode
     *
     * @param mode the neutral mode you want the drive train to use
     */
    private void setNeutralMode(NeutralMode mode) {
        this.leftFrontTalon.setNeutralMode(mode);
        this.leftBackTalon.setNeutralMode(mode);
        this.rightFrontTalon.setNeutralMode(mode);
        this.rightBackTalon.setNeutralMode(mode);
    }
}