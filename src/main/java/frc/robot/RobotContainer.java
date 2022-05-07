// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import frc.robot.auton.AutonSelector;
import frc.robot.commands.DefaultStorageCommand;
import frc.robot.libraries.Gyro1038;
import frc.robot.libraries.Limelight1038;
import frc.robot.subsystems.Dashboard;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.SerialComs;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Storage;
import frc.robot.subsystems.Turret;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {
    private static RobotContainer instance;

    private final int PH_PORT = 1;
    private final int MIN_PRESSURE = 110;
    private final int MAX_PRESSURE = 120;

    public final Compressor compressor = new Compressor(PH_PORT, PneumaticsModuleType.REVPH);

    // Subsystems
    public final Dashboard dashboard = Dashboard.getInstance();
    public final Storage storage = Storage.getInstance();
    public final Turret turret = Turret.getInstance();
    public final Shooter shooter = Shooter.getInstance();
    public final DriveTrain driveTrain = DriveTrain.getInstance();
    public final Gyro1038 gyroSensor = Gyro1038.getInstance();
    public final SerialComs serial = SerialComs.getInstance();
    public final AutonSelector autonSelector = AutonSelector.getInstance();
    public final Limelight1038 limelight = Limelight1038.getInstance();
    public final Elevator elevator = Elevator.getInstance();

    public static RobotContainer getInstance() {
        if (instance == null) {
            System.out.println("Creating a new RobotContainer");
            instance = new RobotContainer();
        }
        return instance;
    }

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    private RobotContainer() {
        serial.register();
        dashboard.register();
        storage.setDefaultCommand(new DefaultStorageCommand());
    }

    public SequentialCommandGroup getSelectedAuton() {
        return autonSelector.chooseAuton();
    }

    public void runCompressor() {
        compressor.enableAnalog(MIN_PRESSURE, MAX_PRESSURE);
    }
}
