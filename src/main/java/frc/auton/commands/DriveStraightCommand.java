package frc.auton.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.libraries.DriveTrain1038;
import frc.libraries.Gyro1038;

public class DriveStraightCommand extends CommandBase {
    private static Gyro1038 gyroSensor = Gyro1038.getInstance();
    private final DriveTrain1038 drive = DriveTrain1038.getInstance();

    private final double END_DRIVE_SPEED = 0.0;
    private final double END_DRIVE_ROTATION = 0.0;
    private final double TOLERANCE = 1.9;

    private final static double dP = 0.001; // .04 proto
    private final static double dI = 0.001;
    private final static double dD = 0.000;
    private static double dSetpoint;
    private final static double tP = 0.200; // .23 proto
    private final static double tI = 0.001;
    private final static double tD = 0.000;

    private PIDController drivePID;
    private PIDController turnPID;

    /**
     * Drive the robot straight
     *
     * @param setpoint number of feet to drive straight
     */
    public DriveStraightCommand(double setpoint) {
        gyroSensor.reset();
        drivePID = new PIDController(dP, dI, dD);
        turnPID = new PIDController(tP, tI, tD);

        drivePID.setPID(dP, dI, dD);

        // Converts inches to feet
        drivePID.setSetpoint(setpoint / 12);
        System.out.println("setpoint" + drivePID.getSetpoint());
        drivePID.setTolerance(TOLERANCE);
        drivePID.disableContinuousInput();
        // SmartDashboard.putData("Controls/Drive Straight", drivePID);

        // Angle
        turnPID.setTolerance(TOLERANCE);
        turnPID.enableContinuousInput(0, 360);
        // SmartDashboard.putData("Controls/Drive Straight Angle", turnPID);
        addRequirements(drive);
    }
}
