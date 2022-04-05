package frc.auton;

import frc.auton.commands.DriveStraightCommand;

public class ReverseAuton extends Auton {
    public ReverseAuton() {
        super();
        addCommands(new DriveStraightCommand(-4));
    }
}