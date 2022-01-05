package frc.auton;

import frc.auton.commands.DriveStraightCommand;
import frc.auton.commands.ShootCommand;
import frc.auton.commands.TurnCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.auton.commands.AcquireCommand;
import frc.auton.commands.AimCommand;


public class GalacticCommands2 extends Auton{
    public String teamColor = "Blue";
    public GalacticCommands2() {
        super();
        switch(teamColor) {
            case "Blue":
            
            final int FIRST_DRIVE_DIST_BLUE = 120;
            final double SECOND_DRIVE_DIST_BLUE = 84.84;
            final double THIRD_DRIVE_DIST_BLUE = 84.84;
            final int FOURTH_DRIVE_DIST_BLUE = 60;
            final int ACQUIRE_TIME_BLUE = 2;
            final double FIRST_TURN_ANGLE_BLUE = -63.438;
            final double SECOND_TURN_ANGLE_BLUE = 63.367;
            final int THIRD_TURN_ANGLE_BLUE = 0;
                addCommands(
                    new AcquireCommand(0),
                    
                    //go forward until you aquire D^, 13 feet
                    new DriveStraightCommand(FIRST_DRIVE_DIST_BLUE),
                    
                    //run aquisition 
                    new AcquireCommand(ACQUIRE_TIME_BLUE),
                    
                    //face B7
                    new TurnCommand(FIRST_TURN_ANGLE_BLUE),
                    new DriveStraightCommand(SECOND_DRIVE_DIST_BLUE),
                    
                    //run aquisition 
                    new AcquireCommand(ACQUIRE_TIME_BLUE),
                    
                    //Face C9
                    new TurnCommand(SECOND_TURN_ANGLE_BLUE),
                    new DriveStraightCommand(THIRD_DRIVE_DIST_BLUE),
                    
                    //run aquisition at C9
                    new AcquireCommand(ACQUIRE_TIME_BLUE),
                    
                    //Face endzone
                    new TurnCommand(THIRD_TURN_ANGLE_BLUE),
                    
                    //Forward to endzone, 5.5 feet
                    new DriveStraightCommand(FOURTH_DRIVE_DIST_BLUE)
                );
                break;
            case "Red":
            final int FIRST_DRIVE_DIST_RED = 60;
            final double SECOND_DRIVE_DIST_RED = 84.84;
            final double THIRD_DRIVE_DIST_RED = 84.84;
            final double FOURTH_DRIVE_DIST_RED = 120;
            final int ACQUIRE_TIME_RED = 2;
            final double FIRST_TURN_ANGLE_RED = 45;
            final double SECOND_TURN_ANGLE_RED = -63.438;
            final int THIRD_TURN_ANGLE_RED = 0;
                addCommands(
                    new AcquireCommand(0),
                    //go forward until you aquire B3, 5.5 feet;
                    new DriveStraightCommand(FIRST_DRIVE_DIST_RED),
                    //run aquisition 
                    new AcquireCommand(ACQUIRE_TIME_RED),
                    //face D5
                    new TurnCommand(FIRST_TURN_ANGLE_RED),
                    new DriveStraightCommand(SECOND_DRIVE_DIST_RED),
                    //Acquire
                    new AcquireCommand(ACQUIRE_TIME_RED),
                    //go to b7
                    new TurnCommand(SECOND_TURN_ANGLE_RED),
                    new DriveStraightCommand(THIRD_DRIVE_DIST_RED),
                    //run aquisition at D5
                    new AcquireCommand(ACQUIRE_TIME_RED),
                    //go to endzone
                    new TurnCommand(THIRD_TURN_ANGLE_RED),
                    new DriveStraightCommand(FOURTH_DRIVE_DIST_RED)
                    //Forward to A6, 8.28 feet
                    /*new DriveStraightCommand(THIRD_DRIVE_DIST_RED),
                    //run aquisition at A6
                    new AcquireCommand(ACQUIRE_TIME_RED),
                    //Face endzone
                    new TurnCommand(THIRD_TURN_ANGLE_RED),
                    //Forward to endzone, 13 feet
                    new DriveStraightCommand(FOURTH_DRIVE_DIST_RED)*/
                );
                break;
    }
}
}