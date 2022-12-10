package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;

import frc.robot.libraries.Gyro1038;
import frc.robot.subsystems.DriveTrain;

public class DriveStraightCommand extends PIDCommand {
    // Constants
    private final static int INCHES_IN_FOOT = 12;
    private final double TOLERANCE = 0;
    private final double MAX_OUTPUT = .5;

    // Subsystem Dependencies
    private static Gyro1038 gyroSensor = Gyro1038.getInstance();
    private static DriveTrain drive = DriveTrain.getInstance();

    // PID Controller Setup (Drive)
    private PIDController drivePID;
    private final static double dP = 0.100; // .04 proto
    private final static double dI = 0.000;
    private final static double dD = 0.000;

    // PID Controller Setup (Turn)
    private PIDController turnPID = new PIDController(tP, tI, tD);
    private final static double tP = 0.020; // .23 proto
    private final static double tI = 0.000;
    private final static double tD = 0.000;

    /**
     * Drive the robot straight
     *
     * @param setpoint number of feet to drive straight
     */
    public DriveStraightCommand(double setpoint) {
        super(new PIDController(dP, dI, dD),
                drive::getLeftDriveEncoderDistance,
                0,
                output -> {
                },
                drive);

        drivePID = this.m_controller;
        drivePID.setTolerance(TOLERANCE);
        drivePID.disableContinuousInput();
        drivePID.setSetpoint(setpoint * INCHES_IN_FOOT);

        // Angle
        turnPID.setTolerance(TOLERANCE);
        turnPID.enableContinuousInput(0, 360);
    }

    @Override
    public void initialize() {
        super.initialize();
        turnPID.setSetpoint(gyroSensor.getAngle());
        drive.resetEncoders();
    }

    @Override
    public void execute() {
        double distanceOutputRaw = drivePID.calculate(drive.getLeftDriveEncoderDistance());
        double angleOutputRaw = turnPID.calculate(gyroSensor.getAngle());

        double distanceOutput = MathUtil.clamp(distanceOutputRaw, -MAX_OUTPUT, MAX_OUTPUT);
        double angleOutput = MathUtil.clamp(angleOutputRaw, -MAX_OUTPUT, MAX_OUTPUT);
        // System.out.println(
        // "dist out: " + distanceOutput +
        // " dist set: " + drivePID.getSetpoint() +
        // " dist: " + drive.getLeftDriveEncoderDistance() +
        // " ang out: " + angleOutput +
        // " ang sp: " + turnPID.getSetpoint() +
        // "ang: " + gyroSensor.getAngle());

        usePIDOutput(distanceOutput, angleOutput);
    }

    @Override
    public boolean isFinished() {
        return drivePID.atSetpoint() && turnPID.atSetpoint();
    }

    /**
     * Uses the value that the pid loop calculated
     *
     * @param drivePower Power to drive straight
     * @param turnPower  Power to turn (used to maintain heading)
     */
    protected void usePIDOutput(double drivePower, double turnPower) {
        drive.arcadeDrive(drivePower, turnPower);
    }
}
