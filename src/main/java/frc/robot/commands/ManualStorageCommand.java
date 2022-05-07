package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Storage;

public class ManualStorageCommand extends CommandBase {
    private Storage storage = Storage.getInstance();

    private final double SHUTTLE_MOTOR_SPEED = 0.7;

    private ManualStorageModes selectedMode;

    public enum ManualStorageModes {
        In, Out
    }

    public ManualStorageCommand(ManualStorageModes mode) {
        this.selectedMode = mode;

        this.addRequirements(storage);
    }

    @Override
    public void execute() {
        switch (selectedMode) {
            case In:
                storage.setPower(SHUTTLE_MOTOR_SPEED);
                break;
            case Out:
                storage.setPower(-SHUTTLE_MOTOR_SPEED);
                break;
        }
    }

    @Override
    public void end(boolean interrupted) {
        storage.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
