package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.DriveTrain.DriveModes;

public class ToggleDriveModeCommand extends CommandBase {
    private DriveTrain driveTrain = DriveTrain.getInstance();

    public ToggleDriveModeCommand() {
        this.addRequirements(driveTrain);
    }

    @Override
    public void execute() {
        switch (driveTrain.currentDriveMode) {
            case tankDrive:
                driveTrain.setDriveMode(DriveModes.singleArcadeDrive);
                break;
            case singleArcadeDrive:
                driveTrain.setDriveMode(DriveModes.dualArcadeDrive);
                break;
            case dualArcadeDrive:
                driveTrain.setDriveMode(DriveModes.tankDrive);
                break;
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}