package frc.robot.auton;

import frc.robot.commands.DriveStraightCommand;
import frc.robot.commands.ShootCommand;

public class OneBallAuton extends Auton {
    private final int SECONDS_TO_SHOOT = 3;

    public OneBallAuton() {
        super();

        addCommands(
                new DriveStraightCommand(-5),
                new ShootCommand(SECONDS_TO_SHOOT));
    }
}