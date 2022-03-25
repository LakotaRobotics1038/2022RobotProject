package frc.auton;

import frc.auton.commands.DriveStraightCommand;

public class ForwardAuton extends Auton {
    // FIXME: this name isn't right, what should it be?

    public ForwardAuton() {
        super();
        addCommands(new DriveStraightCommand(2));
    }
}