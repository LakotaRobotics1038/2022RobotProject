package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Turret;
import frc.robot.subsystems.Turret.TurretDirections;

public class ZeroTurretCommand extends CommandBase {
    // Constants
    private static final int TOLERANCE = 50000;

    // Subsystem Dependencies
    private Turret turret = Turret.getInstance();

    public ZeroTurretCommand() {
        this.addRequirements(turret);
    }

    @Override
    public void execute() {
        if (Math.abs(turret.getPosition()) < TOLERANCE) {
            turret.stop();
        } else if (turret.getPosition() > 0) {
            turret.setDirection(TurretDirections.Right);
            turret.move();
        } else if (turret.getPosition() < 0) {
            turret.setDirection(TurretDirections.Left);
            turret.move();
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
