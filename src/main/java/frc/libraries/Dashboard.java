package frc.libraries;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.auton.AutonSelector;
import frc.subsystem.Shooter;

public class Dashboard {
    private static Dashboard dashboard;
    private Shooter shooter = Shooter.getInstance();
    // private Limelight1038 limelight = Limelight1038.getInstance();
    public SendableChooser<String> autoChooser = new SendableChooser<>();
    public SendableChooser<String> startPosition = new SendableChooser<>();

    private final int CAMERA_EXPOSURE = 50;

    private String position;
    private String autonChooser;
    private ShuffleboardTab driversTab;
    private ShuffleboardTab controlsTab;

    private NetworkTableEntry resetGyro;
    private NetworkTableEntry recalGyro;

    public static Dashboard getInstance() {
        if (dashboard == null) {
            System.out.println("Creating a new Dashboard");
            dashboard = new Dashboard();
        }
        return dashboard;
    }

    private Dashboard() {
        driversTab = Shuffleboard.getTab("Drivers");
        controlsTab = Shuffleboard.getTab("Controls");

        driversTab.add("Match Time", -1);
        driversTab.add("Shooter Angle", 0);
        driversTab.add("Shooter speed", .55);
        autoChooser.setDefaultOption("Test Auton", AutonSelector.TestPath);
        driversTab.add("Auton Choices", autoChooser);
        driversTab.add("Start Position", startPosition);

        resetGyro = controlsTab.add("Reset Gyro", false)
                .withWidget(BuiltInWidgets.kBooleanBox)
                .getEntry();
        recalGyro = controlsTab.add("Recal Gyro", false)
                .withWidget(BuiltInWidgets.kBooleanBox)
                .getEntry();
    }

    public void update() {
        driversTab.add("Shooter Angle", shooter.getTurretEncoder());
        driversTab.add("Gyro", Gyro1038.getInstance().getAngle());
        // SmartDashboard.putBoolean("Limelight Can See Target",
        // limelight.canSeeTarget());
        position = startPosition.getSelected();
        autonChooser = autoChooser.getSelected();

        if (resetGyro.getBoolean(false)) {
            Gyro1038.getInstance().reset();
            resetGyro.setBoolean(false);
        }

        if (recalGyro.getBoolean(false)) {
            Gyro1038.getInstance().calibrate();
            recalGyro.setBoolean(false);
        }
    }

    public String getPosition() {
        return position;
    }

    public String getSelectedAuton() {
        return autonChooser;
    }
}
