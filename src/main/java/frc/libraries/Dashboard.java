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
    private NetworkTableEntry shooterSpeed;
    private NetworkTableEntry distance;
    private NetworkTableEntry shooterMult;

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
        autoChooser.setDefaultOption("No Auton", AutonSelector.None);
        autoChooser.addOption("Drive Forward", AutonSelector.ForwardAuto);
        autoChooser.addOption("Drive Backward", AutonSelector.ReverseAuto);

        startPosition.setDefaultOption("Center", AutonSelector.CenterPosition);
        startPosition.addOption("Left", AutonSelector.LeftPosition);
        startPosition.addOption("Right", AutonSelector.RightPosition);

        driversTab.add("Auton Choices", autoChooser)
                .withPosition(0, 0)
                .withSize(2, 1);
        driversTab.add("Start Position", startPosition)
                .withPosition(0, 1)
                .withSize(2, 1);

        matchTime = driversTab.add("Match Time", -1)
                .getEntry();

        shooterAngle = driversTab.add("Shooter Angle", 0)
                .getEntry();

        gyroAngle = driversTab.add("Gyro", 0)
                .withPosition(2, 0)
                .withWidget(BuiltInWidgets.kGyro)
                .getEntry();

        limelightTarget = controlsTab.add("Limelight Target", false)
                .withWidget(BuiltInWidgets.kBooleanBox)
                .getEntry();

        shooterMult = driversTab.add("Shooter Mult", shooter.speedMultiplier)
                .getEntry();

        // Controls
        resetGyro = controlsTab.add("Reset Gyro", false)
                .withPosition(0, 0)
                .withWidget(BuiltInWidgets.kBooleanBox)
                .getEntry();

        recalGyro = controlsTab.add("Recal Gyro", false)
                .withPosition(0, 1)
                .withWidget(BuiltInWidgets.kBooleanBox)
                .getEntry();

        shooterSpeed = controlsTab.add("Shooter Speed", -1)
                .withPosition(1, 0)
                .getEntry();

        distance = driversTab.add("Target Distance", -1).getEntry();
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

        shooter.speedMultiplier = shooterMult.getDouble(shooter.speedMultiplier);
        distance.setDouble(limelight.getTargetDistance());

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
