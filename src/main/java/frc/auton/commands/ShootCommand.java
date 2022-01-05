package frc.auton.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.Timer;
import frc.subsystem.Acquisition;
import frc.subsystem.Shooter;
import frc.subsystem.Storage;
import frc.subsystem.Storage.ManualStorageModes;

public class ShootCommand extends CommandBase {
    private Shooter shooterInstance = Shooter.getInstance();
    private Storage storage = Storage.getInstance();

    private final double END_TIME;

    public ShootCommand(double endTime) {
        END_TIME = endTime;
    }

    @Override
    public void execute() {
        shooterInstance.executeSpeedPID();
        storage.enableManualStorage(ManualStorageModes.Forward);
        storage.periodic();
    }

    @Override
    public void end(boolean interuppted) {
        shooterInstance.shootManually(0);
        shooterInstance.noFeedBall();
    }

    @Override
    public boolean isFinished() {
        return Timer.getMatchTime() <= END_TIME;
    }
}