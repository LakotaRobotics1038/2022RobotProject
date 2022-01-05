package frc.auton.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.subsystem.Storage;
import frc.robot.Robot;
import frc.auton.*;

public class CheckCommand extends CommandBase {
	public static SequentialCommandGroup red;
	public static SequentialCommandGroup blue;

	public CheckCommand() {
	}

    @Override
	public void execute() {
		red = new PathCheckRed();
		blue = new PathCheckBlue();
		if (Storage.redTeam) {
			Robot.schedule.schedule(red);
			System.out.println("Red");
		}
		else{
			Robot.schedule.schedule(blue);
			System.out.println("Blue");
		}
    }
    
	@Override
	public boolean isFinished() {
		Storage.redTeam = false;
		return true;
    }
}
