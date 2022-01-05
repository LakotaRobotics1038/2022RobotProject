package frc.auton;

import frc.auton.commands.DriveStraightCommand;
import frc.auton.commands.TurnCommand;
import frc.auton.commands.AcquireCommand;
import frc.auton.commands.CheckCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;


public class GalacticCommands extends Auton{
    public GalacticCommands() {
        super();
        addCommands(
            new ParallelCommandGroup(
            //go forward until you aquire B3, 5.5 feet
            new DriveStraightCommand(72),
            //run aquisition 
            new AcquireCommand(8)
            ),
            new CheckCommand()
        );
    }
}