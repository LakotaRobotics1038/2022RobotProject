package frc.auton.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.libraries.DriveTrain1038;
import frc.libraries.Gyro1038;

public class TurnCommand extends CommandBase {
    private static Gyro1038 gyroSensor = Gyro1038.getInstance();
    private final DriveTrain1038 drive = DriveTrain1038.getInstance();

    private double drivePower = 0.0;
    private final double TOLERANCE = 5.0;
    private final static double P = 0.035;
    private final static double I = 0.0015;
    private final static double D = 0.0002;

    private PIDController turnPID;

    public TurnCommand(double setpoint) {
        turnPID = new PIDController(P, I, D);

        turnPID.setPID(P, I, D);

        turnPID.setSetpoint(setpoint);
        System.out.println("setpoint" + turnPID.getSetpoint());
        // turnPID.disableContinuousInput();
        SmartDashboard.putData("Controls/turn", turnPID);

        // Angle
        turnPID.setTolerance(TOLERANCE);
        turnPID.enableContinuousInput(0, 360);
        addRequirements(drive);
    }
}
