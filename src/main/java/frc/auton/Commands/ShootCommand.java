package frc.auton.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.Timer;
import frc.subsystem.Shooter;
import frc.subsystem.Storage;
import frc.subsystem.Storage.ManualStorageModes;

public class ShootCommand extends CommandBase {
    private Shooter shooter = Shooter.getInstance();
    private Storage storage = Storage.getInstance();

    private final double END_TIME;

    public ShootCommand(double endTime) {
        END_TIME = endTime;
    }

    @Override
    public void execute() {
        shooter.enable();
        storage.setManualStorage(ManualStorageModes.In);
        storage.periodic();
    }

    @Override
    public void end(boolean interuppted) {
        shooter.shootManually(0);
        shooter.noFeedBall();
        shooter.disable();
    }

    @Override
    public boolean isFinished() {
        return Timer.getMatchTime() <= END_TIME;
    }
}