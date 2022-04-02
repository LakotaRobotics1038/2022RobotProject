package frc.auton;

import frc.auton.commands.DriveStraightCommand;
import frc.auton.commands.ShootCommand;

public class OneBallAuton extends Auton {
    public OneBallAuton() {
        super();

        addCommands(
                new DriveStraightCommand(-5),
                new ShootCommand());
    }
}