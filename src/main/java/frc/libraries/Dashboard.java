package frc.libraries;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.auton.AutonSelector;
import frc.subsystem.Shooter;

public class Dashboard {
    private static Dashboard dashboard;

    private Shooter shooter = Shooter.getInstance();
    private Gyro1038 gyro = Gyro1038.getInstance();
    private Limelight1038 limelight = Limelight1038.getInstance();

    public SendableChooser<String> autoChooser = new SendableChooser<>();
    public SendableChooser<String> startPosition = new SendableChooser<>();

    private String position;
    private String autonChooser;

    private ShuffleboardTab driversTab;
    private ShuffleboardTab controlsTab;

    private NetworkTableEntry resetGyro;
    private NetworkTableEntry recalGyro;
    private NetworkTableEntry gyroAngle;
    private NetworkTableEntry shooterAngle;
    private NetworkTableEntry matchTime;
    private NetworkTableEntry limelightTarget;
    private NetworkTableEntry limelightSetpoint;
    private NetworkTableEntry shooterSpeed;

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

        // Drivers
        autoChooser.setDefaultOption("Forward Auton", AutonSelector.ForwardAuto);
        driversTab.add("Auton Choices", autoChooser);
        driversTab.add("Start Position", startPosition);

        driversTab.add("Match Time", -1);
        shooterAngle = driversTab.add("Shooter Angle", 0)
                .getEntry();

        gyroAngle = driversTab.add("Gyro", 0)
                .withWidget(BuiltInWidgets.kGyro)
                .getEntry();

        limelightTarget = controlsTab.add("Limelight Target", false)
                .withWidget(BuiltInWidgets.kBooleanBox)
                .getEntry();

        // Controls
        resetGyro = controlsTab.add("Reset Gyro", false)
                .withWidget(BuiltInWidgets.kBooleanBox)
                .getEntry();

        recalGyro = controlsTab.add("Recal Gyro", false)
                .withWidget(BuiltInWidgets.kBooleanBox)
                .getEntry();

        shooterSpeed = controlsTab.add("Shooter Speed", -1)
                .getEntry();

        limelightSetpoint = controlsTab.add("Limelight Setpoint", -1)
                .getEntry();
    }

    public void update() {
        // Drivers
        autonChooser = autoChooser.getSelected();
        position = startPosition.getSelected();
        matchTime.setNumber(Timer.getMatchTime());

        shooterAngle.setNumber(shooter.getTurretEncoder());
        gyroAngle.setNumber(gyro.getAngle());
        limelightTarget.setBoolean(limelight.canSeeTarget());
        shooterSpeed.setNumber(shooter.getShooterSpeed());
        limelightTarget.setNumber(limelight.getShooterSetpoint());

        // Controls
        if (resetGyro.getBoolean(false)) {
            gyro.reset();
            resetGyro.setBoolean(false);
        }

        if (recalGyro.getBoolean(false)) {
            gyro.calibrate();
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
