package frc.auton.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.libraries.DriveTrain1038;
import frc.libraries.Gyro1038;

public class TurnCommand extends PIDCommand {
    private static Gyro1038 gyroSensor = Gyro1038.getInstance();
    private static DriveTrain1038 drive = DriveTrain1038.getInstance();
    private final double TOLERANCE = 5.0;
    private final static double P = 0.015;
    private final static double I = 0.000;
    private final static double D = 0.000;

    private PIDController turnPID;

    /**
     * Turns to robot to the specified angle.
     *
     * @param targetAngleDegrees The angle to turn to
     */
    public TurnCommand(double targetAngleDegrees) {
        super(new PIDController(P, I, D),
                gyroSensor::getAngle,
                targetAngleDegrees,
                output -> drive.arcadeDrive(0, output),
                drive);

        turnPID = getController();

        turnPID.enableContinuousInput(0, 360);
        turnPID.setTolerance(TOLERANCE);

        Shuffleboard.getTab("Controls").add("Turn Command PID", turnPID)
                .withWidget(BuiltInWidgets.kPIDCommand);
    }

    @Override
    public boolean isFinished() {
        return turnPID.atSetpoint();
    }
}