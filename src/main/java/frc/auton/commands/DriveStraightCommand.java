package frc.auton.commands;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.subsystem.DriveTrain;
import frc.subsystem.Storage;
import frc.robot.Gyro1038;

public class DriveStraightCommand extends CommandBase {
	private static Gyro1038 gyroSensor = Gyro1038.getInstance();
	private final DriveTrain drive = DriveTrain.getInstance();

	private final double END_DRIVE_SPEED = 0.0;
	private final double END_DRIVE_ROTATION = 0.0;
	private final double TOLERANCE = 1.9;

	private final static double dP = 0.016; // .002
	private final static double dI = 0.008; //.0025
	private final static double dD = 0.0000; //.0001
	private static double dSetpoint;
	private final static double tP = 0.200; // .23 proto
	private final static double tI = 0.001;
	private final static double tD = 0.000;
	
	private PIDController drivePID; 
	private PIDController turnPID;

	/**
	 * Makes a new Drive Straight Command
	 * 
	 * @param setpoint in feet
	 */
	public DriveStraightCommand(double setpoint) {
		gyroSensor.reset();
		drivePID = new PIDController(dP,dI,dD);
		turnPID = new PIDController(tP, tI, tD);

		drivePID.setPID(dP, dI, dD);


		// *12 Converts inches to feet
		dSetpoint = setpoint;
		drivePID.setSetpoint(setpoint);
		System.out.println("setpoint" + drivePID.getSetpoint());
		drivePID.setTolerance(TOLERANCE);
		drivePID.disableContinuousInput();
		SmartDashboard.putData("Controls/Drive Straight", drivePID);

		// Angle
		turnPID.setTolerance(TOLERANCE);
		turnPID.enableContinuousInput(0, 360);
		SmartDashboard.putData("Controls/Drive Straight Angle", turnPID);
		addRequirements(drive);
	}
	public DriveStraightCommand(double RedSetpoint, double BlueSetpoint) {
		double setpoint = 0.0;
		if(Storage.redTeam) {
			setpoint = RedSetpoint;
		}
		else {
			setpoint = BlueSetpoint;
		}
		gyroSensor.reset();
		drivePID = new PIDController(dP,dI,dD);
		turnPID = new PIDController(tP, tI, tD);

		drivePID.setPID(dP, dI, dD);


		// *12 Converts inches to feet
		dSetpoint = setpoint;
		drivePID.setSetpoint(setpoint);
		System.out.println("setpoint" + drivePID.getSetpoint());
		drivePID.setTolerance(TOLERANCE);
		drivePID.disableContinuousInput();
		SmartDashboard.putData("Controls/Drive Straight", drivePID);

		// Angle
		turnPID.setTolerance(TOLERANCE);
		turnPID.enableContinuousInput(0, 360);
		SmartDashboard.putData("Controls/Drive Straight Angle", turnPID);
		addRequirements(drive);
	}

	@Override
	public void initialize() {
		gyroSensor.reset();
		turnPID.setSetpoint(gyroSensor.getAngle());
		drive.resetEncoders();
		// drivePID.setSetpoint(dSetpoint);
		// System.out.println("setpoint" + drivePID.getSetpoint());
		// turnPID.setInputRange(0, 359);
	}

	@Override
	public void execute() {
		//System.out.println(Storage.redTeam);
		double distancePID = drivePID.calculate(drive.getLeftDriveEncoderDistance());
		// double anglePID = turnPID.calculate(gyroSensor.getAngle());
		// TODO: incorperate the turnPID calculate.
		drive.tankDrive(distancePID, distancePID);
	
		//System.out.println("power: " + distancePID + " encoders: " + drive.getLeftDriveEncoderDistance());
	}

	@Override
	public void end(boolean interrupted) {
		drivePID.reset();
		turnPID.reset();
		drive.tankDrive(END_DRIVE_SPEED, END_DRIVE_ROTATION);
		System.out.println("DriveStraight ended");
	}

	@Override
	public boolean isFinished() {
		return drivePID.atSetpoint() && turnPID.atSetpoint();
	}
}