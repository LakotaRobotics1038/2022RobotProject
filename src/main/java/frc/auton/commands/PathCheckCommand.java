package frc.auton.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.subsystem.Storage;
import frc.robot.Robot;
import frc.auton.*;

public class PathCheckCommand extends CommandBase {
	public static SequentialCommandGroup red1;
	public static SequentialCommandGroup blue1;
	public static SequentialCommandGroup red2;
	public static SequentialCommandGroup blue2;
	public char alliance = 'N';

	public PathCheckCommand(char team) {
		alliance = team;
	}

    @Override
	public void execute() {
		red1 = new PathOneRed();
		blue1 = new PathOneBlue();
		red2 = new PathTwoRed();
		blue2 = new PathTwoBlue();
		if (alliance=='R')
		{
			if (Storage.redTeam) {
				Robot.schedule.schedule(red1);
				System.out.println("Red 1");
			}
			else{
				Robot.schedule.schedule(red2);
				System.out.println("Red 2");
			}
		}
		else if (alliance=='B')
		{
			if (Storage.redTeam) {
				Robot.schedule.schedule(blue1);
				System.out.println("Blue 1");
			}
			else{
				Robot.schedule.schedule(blue2);
				System.out.println("Blue 2");
			}
		}
    }
    
	@Override
	public boolean isFinished() {
		return true;
    }
}
