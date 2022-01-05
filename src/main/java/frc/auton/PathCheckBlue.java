package frc.auton;

import frc.auton.commands.DriveStraightCommand;
import frc.auton.commands.TurnCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.auton.commands.AcquireCommand;
import frc.auton.commands.PathCheckCommand;


public class PathCheckBlue extends Auton{
    public PathCheckBlue() {
        super();
        addCommands(
            //Gets to and acquires first blue ball
            new TurnCommand(72),
            new ParallelCommandGroup(
            new DriveStraightCommand(76),
            //run aquisition 
            new AcquireCommand(8)
            ),
            //Gets to possible second blue ball
            new TurnCommand(110),
            new ParallelCommandGroup(
            new DriveStraightCommand(39),
            //run aquisition 
            new AcquireCommand(8)
            ),
            //Checks which blue path
            new PathCheckCommand('B')
        );
    }
}