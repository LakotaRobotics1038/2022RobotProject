/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.auton.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.subsystem.Acquisition;
import frc.subsystem.Storage;

public class AcquireCommand extends CommandBase {
  // private double START_TIME = 0;

  Acquisition acquisition = Acquisition.getInstance();
  Storage storage = Storage.getInstance();
  Timer timer = new Timer();

  private final double END_TIME;
  private final double START_TIME;
  public boolean redTeam;
  
  /**
   * Creates a new MoveAcquisitionCommand.
   */
  public AcquireCommand(double endTime) {
    START_TIME = Timer.getFPGATimestamp();
    END_TIME = endTime;
  }

  public AcquireCommand(double endTimeRed, double endTimeBlue) {
    double setpoint = 0.0;
		if(Storage.redTeam) {
			END_TIME = endTimeRed;
		}
		else {
			END_TIME = endTimeBlue;
		}
    START_TIME = Timer.getFPGATimestamp();
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    acquisition.down();
    timer.reset();
    timer.start();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    System.out.println(storage.shuttleMotorEncoder.getPosition());
    storage.periodic();
    acquisition.runBeaterBarFwd();
    System.out.println(timer.get());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    System.out.println(storage.shuttleMotorEncoder.getPosition());
    acquisition.stopBeaterBar();
    storage.feedShooter(0.0);
    storage.shuttleMotorEncoder.setPosition(547);
    timer.stop();
    System.out.println("End Acquire");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    //return (Timer.getFPGATimestamp() - START_TIME) <= END_TIME;
    //boolean finished = Timer.hasPeriodPassed(END_TIME);\
    return (timer.get() >= END_TIME || (storage.shuttleMotorEncoder.getPosition() > 170 && storage.shuttleMotorEncoder.getPosition() <= 500));
    //Timer.getFPGATimestamp()
  }
}
