package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.auton.AutonSelector;
import frc.robot.libraries.Gyro1038;
import frc.robot.libraries.Limelight1038;

public class Dashboard implements Subsystem {
    private static Dashboard instance;

    private Shooter shooter = Shooter.getInstance();
    private Gyro1038 gyro = Gyro1038.getInstance();
    private Limelight1038 limelight = Limelight1038.getInstance();

    public SendableChooser<String> autoChooser = new SendableChooser<>();
    public SendableChooser<String> startPosition = new SendableChooser<>();

    private String position;
    private String autonChooser;

    private ShuffleboardTab driversTab = Shuffleboard.getTab("Drivers");
    private ShuffleboardTab controlsTab = Shuffleboard.getTab("Controls");

    // Drivers
    private NetworkTableEntry shooterMult = driversTab.add("Shooter Mult", shooter.speedMultiplier)
            .withSize(2, 1)
            .withPosition(5, 0)
            .getEntry();

    // Controls
    private NetworkTableEntry resetGyro = controlsTab.add("Reset Gyro", false)
            .withSize(1, 1)
            .withPosition(1, 1)
            .withWidget(BuiltInWidgets.kToggleButton)
            .getEntry();
    private NetworkTableEntry recalibrateGyro = controlsTab.add("Recal Gyro", false)
            .withSize(1, 1)
            .withPosition(0, 1)
            .withWidget(BuiltInWidgets.kToggleButton)
            .getEntry();

    public static Dashboard getInstance() {
        if (instance == null) {
            System.out.println("Creating a new Dashboard");
            instance = new Dashboard();
        }
        return instance;
    }

    private Dashboard() {
        Shuffleboard.selectTab("Drivers");
        // Drivers
        autoChooser.setDefaultOption("No Auton", AutonSelector.None);
        autoChooser.addOption("Drive Forward", AutonSelector.ForwardAuto);
        autoChooser.addOption("Drive Backward", AutonSelector.ReverseAuto);
        autoChooser.addOption("One Ball", AutonSelector.OneBallAuto);

        startPosition.setDefaultOption("Center", AutonSelector.CenterPosition);
        startPosition.addOption("Left", AutonSelector.LeftPosition);
        startPosition.addOption("Right", AutonSelector.RightPosition);

        // Drivers
        driversTab.add("Auton Choices", autoChooser)
                .withPosition(0, 0)
                .withSize(2, 1);
        driversTab.add("Start Position", startPosition)
                .withPosition(0, 1)
                .withSize(2, 1);

        driversTab.addNumber("Shooter Angle", shooter::getTurretEncoder)
                .withPosition(2, 2)
                .withSize(1, 1);

        driversTab.addNumber("Gyro", gyro::getAngle)
                .withPosition(2, 0);
        // .withWidget(BuiltInWidgets.kGyro);

        driversTab.addNumber("Target Distance", limelight::getTargetDistance)
                .withPosition(5, 1)
                .withSize(2, 1);

        // Controls
        controlsTab.addBoolean("Limelight Target", limelight::canSeeTarget)
                .withWidget(BuiltInWidgets.kBooleanBox);

        controlsTab.addNumber("Shooter Speed", shooter::getShooterSpeed)
                .withPosition(1, 0);

    }

    @Override
    public void periodic() {
        // Drivers
        autonChooser = autoChooser.getSelected();
        position = startPosition.getSelected();

        shooter.speedMultiplier = shooterMult.getDouble(shooter.speedMultiplier);

        // Controls
        if (resetGyro.getBoolean(false)) {
            gyro.reset();
            resetGyro.setBoolean(false);
        }

        if (recalibrateGyro.getBoolean(false)) {
            gyro.calibrate();
            recalibrateGyro.setBoolean(false);
        }
    }

    public String getPosition() {
        return position;
    }

    public String getSelectedAuton() {
        return autonChooser;
    }
}
