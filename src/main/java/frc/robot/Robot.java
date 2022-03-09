/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.Map;

import javax.print.attribute.standard.MediaSize.Engineering;

import edu.wpi.first.wpilibj.TimedRobot;
import frc.libraries.*;
import frc.subsystem.*;

import frc.libraries.Joystick1038;
/*
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */

public class Robot extends TimedRobot {
  private final DriveTrain1038 driveTrain = DriveTrain1038.getInstance();
  private final Acquisition acquisition = Acquisition.getInstance();
  private final Shooter shooter = Shooter.getInstance();
  private final Endgame endgame = Endgame.getInstance();
  private final SerialComs rpiComs = SerialComs.getInstance();
  private final Map1038 map = Map1038.getInstance();

  private Joystick1038 driverJoystick = new Joystick1038(0);
  private Joystick1038 operatorJoystick = new Joystick1038(1);

  // private Driver driverJoystick = Driver.getInstance();
  // private Operator operatorJoystick = Operator.getInstance();

  /*
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */

  @Override
  public void robotInit() {
    // rpiComs.stopSerialPort();
  }

  @Override
  public void robotPeriodic() {
  }

  public void teleopInit() {
    rpiComs.initialize();
  }

  public void teleopPeriodic() {
    boolean prevXButtonState = false;
    driveTrain.tankDrive(driverJoystick.getLeftJoystickVertical(), driverJoystick.getRightJoystickVertical());

    if (operatorJoystick.getAButton()) {
      acquisition.toggleAcqPos();
    }

    if (operatorJoystick.getXButton() && !prevXButtonState) {
      endgame.liftElevator();
      prevXButtonState = true;
    } else {
      endgame.lowerElevator();
      prevXButtonState = false;
    }

  }

  public void autonomousInit() {
  }

  public void autonomousPeriodic() {
  }

  public void disabledInit() {
  }

  public void disabledPeriodic() {
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testInit() {

  }

  @Override
  public void testPeriodic() {
    // if (operatorJoystick.getAButton()) {
    // testCylinder.set(Value.kForward);
    // } else if (operatorJoystick.getBButton()) {
    // testCylinder.set(Value.kReverse);
    // }
    // driveTrain.tankDrive(driverJoystick.getRightTrigger(),
    // driverJoystick.getRightTrigger());

  }
}
