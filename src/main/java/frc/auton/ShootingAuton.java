package frc.auton;

import frc.auton.commands.DriveStraightCommand;
import frc.auton.commands.ShootCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.auton.commands.AimCommand;

public class ShootingAuton extends Auton {
    public ShootingAuton() {
        super();

        addCommands(
                new AimCommand(11),
                new ParallelCommandGroup(
                        new ShootCommand(6),
                        new AimCommand(6)),
                new DriveStraightCommand(200));
    }
}