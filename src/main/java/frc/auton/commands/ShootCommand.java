package frc.auton.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.Timer;
import frc.subsystem.Shooter;
import frc.subsystem.Storage;

public class ShootCommand extends CommandBase {
    private final int SECONDS_TO_SHOOT = 3;
    private Shooter shooter = Shooter.getInstance();
    private Storage storage = Storage.getInstance();

    private double startTime = -1;

    public ShootCommand() {
        this.addRequirements(shooter, storage);
    }

    @Override
    public void initialize() {
        shooter.enable();
    }

    @Override
    public void execute() {
        shooter.findTarget();
        shooter.feedBall();
        if (shooter.isFinished()) {
            startTime = Timer.getFPGATimestamp();
        }
    }

    @Override
    public void end(boolean interrupted) {
        storage.disableManualStorage();
        shooter.disable();
    }

    @Override
    public boolean isFinished() {
        return shooter.isFinished() &&
                Timer.getFPGATimestamp() + SECONDS_TO_SHOOT > startTime;
    }
}