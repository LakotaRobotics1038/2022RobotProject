package frc.robot.auton;

import frc.robot.commands.DriveStraightCommand;

public class ForwardAuton extends Auton {
    public ForwardAuton() {
        super();
        addCommands(new DriveStraightCommand(4));
    }
}