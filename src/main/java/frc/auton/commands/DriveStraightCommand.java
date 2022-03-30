package frc.auton.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.libraries.DriveTrain1038;
import frc.libraries.Gyro1038;

public class DriveStraightCommand extends PIDCommand {

    private final double TOLERANCE = 1.9;
    private final double MAX_OUTPUT = .8;
    private final static double dP = 0.150; // .04 proto
    private final static double dI = 0.000;
    private final static double dD = 0.002;
    private final static double tP = 0.200; // .23 proto
    private final static double tI = 0.001;
    private final static double tD = 0.000;
    private static Gyro1038 gyroSensor = Gyro1038.getInstance();
    private static DriveTrain1038 drive = DriveTrain1038.getInstance();
    private PIDController drivePID;
    private PIDController turnPID = new PIDController(tP, tI, tD);

    /**
     * Drive the robot straight
     *
     * @param setpoint number of feet to drive straight
     */
    public DriveStraightCommand(double setpoint) {
        super(new PIDController(dP, dI, dD),
                drive::getLeftDriveEncoderDistance,
                setpoint * 12,
                null,
                drive);

        drivePID = getController();
        drivePID.setTolerance(TOLERANCE);
        drivePID.disableContinuousInput();

        // Angle
        turnPID.setTolerance(TOLERANCE);
        turnPID.enableContinuousInput(0, 360);
    }

    @Override
    public void initialize() {
        turnPID.setSetpoint(gyroSensor.getAngle());
        drive.resetEncoders();
    }

    @Override
    public void execute() {
        double distanceOutputRaw = drivePID.calculate(drive.getLeftDriveEncoderDistance());
        double angleOutputRaw = turnPID.calculate(gyroSensor.getAngle());

        double distanceOutput = MathUtil.clamp(distanceOutputRaw, -MAX_OUTPUT, MAX_OUTPUT);
        double angleOutput = MathUtil.clamp(angleOutputRaw, -MAX_OUTPUT, MAX_OUTPUT);
        System.out.println(
                "dist out: " + distanceOutput +
                        " ang out: " + angleOutput +
                        " ang sp: " + turnPID.getSetpoint() +
                        "ang: " + gyroSensor.getAngle());

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
