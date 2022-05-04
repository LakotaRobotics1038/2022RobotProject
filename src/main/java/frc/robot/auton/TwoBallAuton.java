package frc.robot.auton;

import frc.robot.commands.DriveStraightCommand;
import frc.robot.commands.ShootCommand;
import frc.robot.commands.AcquireCommand.Modes;
import frc.robot.commands.AcquireCommand;

public class TwoBallAuton extends Auton {
    public TwoBallAuton() {
        super();

        addCommands(
                new DriveStraightCommand(-5),
                new AcquireCommand(Modes.Acquire, 2),
                new ShootCommand());
    }
}