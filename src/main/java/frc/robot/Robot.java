/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.libraries.Dashboard;
import frc.subsystem.SerialComs;
import frc.subsystem.Storage;
import frc.subsystem.*;

/*
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
    private final int PH_PORT = 1;
    private final int MIN_PRESSURE = 115;
    private final int MAX_PRESSURE = 120;

    private final SerialComs rpiComs = SerialComs.getInstance();
    private final Compressor compressor = new Compressor(PH_PORT, PneumaticsModuleType.REVPH);
    private final Shooter shooter = Shooter.getInstance();

    /*
     * This function is run when the robot is first started up and should be used
     * for any initialization code.
     */

    @Override
    public void robotInit() {
        // rpiComs.stopSerialPort();
        shooter.resetTurretEncoder();
    }

    @Override
    public void robotPeriodic() {

        Dashboard.getInstance().update();
        // System.out.println(compressor.getPressure());
        System.out.println(shooter.turretMotor.getSelectedSensorPosition());

    }

    public void teleopInit() {

    }

    public void teleopPeriodic() {
        // compressor.enableAnalog(MIN_PRESSURE, MAX_PRESSURE);
        Driver.getInstance().periodic();
        Operator.getInstance().periodic();
        Storage.getInstance().periodic();
    }

    public void autonomousInit() {
    }

    public void autonomousPeriodic() {
        compressor.enableAnalog(MIN_PRESSURE, MAX_PRESSURE);
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

    }
}
