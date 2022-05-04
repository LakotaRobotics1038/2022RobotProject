package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.DriveTrain.GearStates;

public class SetDriveGearStateCommand extends CommandBase {
    private DriveTrain driveTrain;
    private GearStates state;

    public SetDriveGearStateCommand(DriveTrain driveTrain, GearStates state) {
        this.driveTrain = driveTrain;
        this.state = state;
        this.addRequirements(driveTrain);
    }

    @Override
    public void execute() {
        this.driveTrain.setGearState(state);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}