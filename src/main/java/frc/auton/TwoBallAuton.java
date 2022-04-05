package frc.auton;

import frc.auton.commands.DriveStraightCommand;
import frc.auton.commands.ShootCommand;
import frc.auton.commands.AcquireCommand.Modes;
import frc.auton.commands.AcquireCommand;

public class TwoBallAuton extends Auton {
    public TwoBallAuton() {
        super();

        addCommands(
                new DriveStraightCommand(-5),
                new AcquireCommand(Modes.Acquire, 2),
                new ShootCommand());
    }
}