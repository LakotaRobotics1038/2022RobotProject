package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.Turret.TurretDirections;

public class ZeroTurretCommand extends CommandBase {
    private Turret turret = Turret.getInstance();
    private static final int TOLERANCE = 50000;

    public ZeroTurretCommand() {
        this.addRequirements(turret);
    }

    @Override
    public void execute() {
        if (turret.getPosition() > 0) {
            turret.setDirection(TurretDirections.Right);
            turret.move();
        } else if (turret.getPosition() < 0) {
            turret.setDirection(TurretDirections.Left);
            turret.move();
        }
    }

    @Override
    public void end(boolean interrupted) {
        turret.stop();
    }

    @Override
    public boolean isFinished() {
        return Math.abs(turret.getPosition()) < TOLERANCE;
    }
}
