package frc.auton;

import frc.auton.commands.DriveStraightCommand;

public class ForwardAuton extends Auton {
    public ForwardAuton() {
        super();
        addCommands(new DriveStraightCommand(4));
    }
}