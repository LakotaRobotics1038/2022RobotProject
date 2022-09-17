package frc.robot.auton;

import frc.robot.commands.DriveStraightCommand;

public class ReverseAuton extends Auton {
    public ReverseAuton() {
        super();
        addCommands(new DriveStraightCommand(-4));
    }
}