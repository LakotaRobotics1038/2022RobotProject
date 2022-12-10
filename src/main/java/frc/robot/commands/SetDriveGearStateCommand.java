package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.DriveTrain.GearStates;

public class SetDriveGearStateCommand extends CommandBase {
    // Subsystem Dependencies
    private DriveTrain driveTrain = DriveTrain.getInstance();

    // States
    private GearStates state;

    public SetDriveGearStateCommand(GearStates state) {
        this.state = state;
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