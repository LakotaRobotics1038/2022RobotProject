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
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.auton.AutonSelector;
import frc.libraries.Dashboard;
import frc.libraries.DriveTrain1038;
import frc.libraries.Gyro1038;
import frc.libraries.Limelight1038;
import frc.libraries.Limelight1038.LEDStates;
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
    private final int MIN_PRESSURE = 110;
    private final int MAX_PRESSURE = 120;

    private final Compressor compressor = new Compressor(PH_PORT, PneumaticsModuleType.REVPH);
    private final Storage storage = Storage.getInstance();
    private final Shooter shooter = Shooter.getInstance();
    private final DriveTrain1038 driveTrain = DriveTrain1038.getInstance();
    private final Gyro1038 gyroSensor = Gyro1038.getInstance();
    private final CommandScheduler scheduler = CommandScheduler.getInstance();
    private final AutonSelector autonSelector = AutonSelector.getInstance();
    private final Limelight1038 limelight = Limelight1038.getInstance();
    private final Endgame endgame = Endgame.getInstance();
    private SequentialCommandGroup autonPath;

    /*
     * This function is run when the robot is first started up and should be used
     * for any initialization code.
     */
    @Override
    public void robotInit() {
        shooter.resetTurretEncoder();
        limelight.changeLEDStatus(LEDStates.Off);
    }

    @Override
    public void robotPeriodic() {
        Dashboard.getInstance().update();
    }

    public void teleopInit() {
        driveTrain.setCoastMode();
    }

    public void teleopPeriodic() {
        compressor.enableAnalog(MIN_PRESSURE, MAX_PRESSURE);
        Driver.getInstance().periodic();
        Operator.getInstance().periodic();
        storage.periodic();
        shooter.periodic();
    }

    public void autonomousInit() {
        driveTrain.setBrakeMode();
        autonPath = autonSelector.chooseAuton();
        gyroSensor.reset();
        scheduler.schedule(autonPath);
    }

    public void autonomousPeriodic() {
        compressor.enableAnalog(MIN_PRESSURE, MAX_PRESSURE);
        scheduler.run();
    }

    public void disabledInit() {
        endgame.engageRatchet();
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
