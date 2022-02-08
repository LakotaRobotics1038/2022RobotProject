/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import frc.libraries.*;
import frc.subsystem.*;

import edu.wpi.first.wpilibj.Joystick;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsControlModule;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

/*
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */

public class Robot extends TimedRobot {
  Joystick1038 driverJoystick = new Joystick1038(0);
  public Joystick1038 operatorJoystick = new Joystick1038(1);
  public SerialComs rpiComs = SerialComs.getInstance();

  public int testIn = 0;
  public int testOut = 1;
  public int testMotorPort = 0;
  public DoubleSolenoid testCylinder = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, testIn, testOut);

  public TalonFX1038 testMotor = new TalonFX1038(testMotorPort);

  private final DriveTrain1038 driveTrain = DriveTrain1038.getInstance();

  /*
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */

  @Override
  public void robotInit() {
  }

  @Override
  public void robotPeriodic() {
  }

  public void teleopInit() {
    // rpiComs.stopSerialPort();
    rpiComs.initialize();
  }

  public void teleopPeriodic() {
    driveTrain.tankDrive(driverJoystick.getLeftJoystickVertical() * -.8,
        driverJoystick.getRightJoystickVertical() * -.8);

    rpiComs.testRead();

    // final int talonTesting_port_1 = 55

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
    if (operatorJoystick.getAButton()) {
      testCylinder.set(Value.kForward);
    } else if (operatorJoystick.getBButton()) {
      testCylinder.set(Value.kReverse);
    }
    driveTrain.tankDrive(driverJoystick.getRightTrigger(), driverJoystick.getRightTrigger());

  }
}
