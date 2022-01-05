package frc.auton;

import frc.auton.commands.DriveStraightCommand;
import frc.auton.commands.TurnCommand;
import frc.auton.commands.AcquireCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;


public class PathOneBlue extends Auton{
    public PathOneBlue() {
        super();
        final int FIRST_DRIVE_DIST_BLUE = 103;      
        final double SECOND_DRIVE_DIST_BLUE = 76;
        final double THIRD_DRIVE_DIST_BLUE = 50;
        final int FOURTH_DRIVE_DIST_BLUE = 96;
        final double ACQUIRE_TIME_BLUE = 10;
        final double ZEROTH_TURN_ANGLE = 30;         
        final double FIRST_TURN_ANGLE_BLUE = 260;
        final double SECOND_TURN_ANGLE_BLUE = 80;
        final int THIRD_TURN_ANGLE_BLUE = 350;
        addCommands(
            new TurnCommand(ZEROTH_TURN_ANGLE),
            new ParallelCommandGroup(
                //go forward until you aquire E6, 13 feet
                new DriveStraightCommand(FIRST_DRIVE_DIST_BLUE),
                //run aquisition at E6
                new AcquireCommand(ACQUIRE_TIME_BLUE)
            ),
            //face B7
            new TurnCommand(FIRST_TURN_ANGLE_BLUE),
            new ParallelCommandGroup(
                //Forward to B7, 8.28 feet
                new DriveStraightCommand(SECOND_DRIVE_DIST_BLUE),
                //run aquisition at B7
                new AcquireCommand(ACQUIRE_TIME_BLUE)
            ),
            //Face C9
            new TurnCommand(SECOND_TURN_ANGLE_BLUE),
            new ParallelCommandGroup(
                //Forward to C9, 5.59 feet
                new DriveStraightCommand(THIRD_DRIVE_DIST_BLUE),
                //run aquisition at C9
                new AcquireCommand(ACQUIRE_TIME_BLUE)
            ),
            //Face endzone
            new TurnCommand(THIRD_TURN_ANGLE_BLUE),
            //Forward to endzone, 5.5 feet
            new DriveStraightCommand(FOURTH_DRIVE_DIST_BLUE)
        );
    }
}