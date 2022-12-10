package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;

import frc.robot.libraries.Gyro1038;
import frc.robot.subsystems.DriveTrain;

public class TurnCommand extends PIDCommand {
    // Subsystem Dependencies
    private static DriveTrain drive = DriveTrain.getInstance();

    // Inputs
    private static Gyro1038 gyroSensor = Gyro1038.getInstance();

    // PID Controller Setup
    private PIDController turnPID;
    private final double TOLERANCE = 3.0;
    private final static double P = 0.015;
    private final static double I = 0.000;
    private final static double D = 0.000;

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

        turnPID = this.m_controller;

        turnPID.enableContinuousInput(0, 360);
        turnPID.setTolerance(TOLERANCE);
    }

    @Override
    public boolean isFinished() {
        return turnPID.atSetpoint();
    }
}