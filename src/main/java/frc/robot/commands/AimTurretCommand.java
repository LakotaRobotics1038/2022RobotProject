package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.libraries.Limelight1038;
import frc.robot.libraries.Limelight1038.LEDStates;
import frc.robot.subsystems.Turret;

public class AimTurretCommand extends CommandBase {
    private static Turret turret = Turret.getInstance();
    private static Limelight1038 limelight = Limelight1038.getInstance();
    private final static double POWER_MULTIPLIER = 0.5;
    private final static double TOLERANCE = 10.0;
    private final static double SETPOINT = 0.0;
    private final static double P = 0.08; // .15
    private final static double I = 0.0;
    private final static double D = 0.0;

    private PIDController aimPID;

    /**
     * Turns to robot to the specified angle.
     */
    public AimTurretCommand() {
        aimPID = new PIDController(P, I, D);

        aimPID.setSetpoint(SETPOINT);
        aimPID.disableContinuousInput();
        aimPID.setTolerance(TOLERANCE);

        addRequirements(turret);
    }

    @Override
    public void execute() {
        limelight.changeLEDStatus(LEDStates.On);

        if (limelight.canSeeTarget()) {
            double power = aimPID.calculate(limelight.getXOffset());
            turret.setPower(power * POWER_MULTIPLIER);
        } else {
            turret.move();
        }
    }

    @Override
    public void end(boolean interrupted) {
        limelight.changeLEDStatus(LEDStates.Off);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}