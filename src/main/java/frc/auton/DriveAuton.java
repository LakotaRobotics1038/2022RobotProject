package frc.auton;

import frc.auton.commands.DriveStraightCommand;

public class DriveAuton extends Auton {
    // FIXME: this name isn't right, what should it be?
    
	public DriveAuton() {
        super();
        addCommands(new DriveStraightCommand(5));
    }
}