package frc.auton;

import frc.auton.commands.DriveStraightCommand;
import frc.auton.commands.TurnCommand;
import frc.auton.commands.AcquireCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;


public class PathTwoRed extends Auton{
    public PathTwoRed() {
        super();
        final double SECOND_DRIVE_DIST_RED = 20;
        final double THIRD_DRIVE_DIST_RED = 84.00;
        final double FOURTH_DRIVE_DIST_RED = 100;
        final double ACQUIRE_TIME_RED = 5;
        final double FIRST_TURN_ANGLE_RED = 40;
        final double SECOND_TURN_ANGLE_RED = 295.00;
        final int THIRD_TURN_ANGLE_RED = 10;
        addCommands(
            new TurnCommand(FIRST_TURN_ANGLE_RED),
            new ParallelCommandGroup(
                new DriveStraightCommand(SECOND_DRIVE_DIST_RED),
                //run aquisition at D5
                new AcquireCommand(ACQUIRE_TIME_RED)
            ),
            //Face A6
            new TurnCommand(SECOND_TURN_ANGLE_RED),
            new ParallelCommandGroup(
                //Forward to A6, 8.28 feet
                new DriveStraightCommand(THIRD_DRIVE_DIST_RED),
                //run aquisition at A6
                new AcquireCommand(ACQUIRE_TIME_RED)
            ),
            //Face endzone
            new TurnCommand(THIRD_TURN_ANGLE_RED),
            //Forward to endzone, 13 feet
            new DriveStraightCommand(FOURTH_DRIVE_DIST_RED)
        );
    }
}