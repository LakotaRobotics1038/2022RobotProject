package frc.auton;

import frc.auton.commands.DriveStraightCommand;
import frc.auton.commands.TurnCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.auton.commands.AcquireCommand;
import frc.auton.commands.PathCheckCommand;


public class PathCheckRed extends Auton{
    public PathCheckRed() {
        super();
        addCommands(
            new TurnCommand(20),
            new ParallelCommandGroup(
            new DriveStraightCommand(62),
            //run aquisition 
            new AcquireCommand(8)
            ),
            new PathCheckCommand('R')
        );
    }
}
