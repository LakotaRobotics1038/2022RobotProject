package frc.auton;

import frc.auton.commands.DriveStraightCommand;
import frc.auton.commands.TurnCommand;
import frc.auton.commands.AcquireCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;


public class PathTwoBlue extends Auton{
    public PathTwoBlue() {
        super();
        final int FIRST_DRIVE_DIST_BLUE = 127;        ///////CHANGE
        final double SECOND_DRIVE_DIST_BLUE = 84.84;
        final double THIRD_DRIVE_DIST_BLUE = 64.84;
        final int FOURTH_DRIVE_DIST_BLUE = 60;
        final double ACQUIRE_TIME_BLUE = 100;
        final double ZEROTH_TURN_ANGLE_BLUE = 30;   //////CHANGE
        final double FIRST_TURN_ANGLE_BLUE = 325.00;//////CHANGE
        final double SECOND_TURN_ANGLE_BLUE = 70.00;
        final int THIRD_TURN_ANGLE_BLUE = 330;
        addCommands(
            new TurnCommand(ZEROTH_TURN_ANGLE_BLUE),
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