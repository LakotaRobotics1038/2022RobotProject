package frc.libraries;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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

    public static Dashboard getInstance() {
        if (dashboard == null) {
            System.out.println("Creating a new Dashboard");
            dashboard = new Dashboard();
        }
        return dashboard;
    }

    private Dashboard() {
        SmartDashboard.putNumber("Drivers/Match Time", -1);
        SmartDashboard.putNumber("Drivers/Shooter Angle", 0);
        SmartDashboard.putNumber("Drivers/Shooter speed", .55);

        autoChooser.setDefaultOption("Test Auton", AutonSelector.TestPath);
        SmartDashboard.putData("Drivers/Start Position", startPosition);
        SmartDashboard.putData("Drivers/Auton choices", autoChooser);
    }

    public void update() {
        SmartDashboard.putNumber("Drivers/Shooter Angle", shooter.getTurretEncoder());
        SmartDashboard.putNumber("Drivers/Gyro", Gyro1038.getInstance().getAngle());
        // SmartDashboard.putBoolean("Limelight Can See Target",
        // limelight.canSeeTarget());
        position = startPosition.getSelected();
        autonChooser = autoChooser.getSelected();

        if (SmartDashboard.getBoolean("Controls/Reset Gyro", false)) {
            Gyro1038.getInstance().reset();
            SmartDashboard.putBoolean("Controls/Reset Gyro", false);
        }

        if (SmartDashboard.getBoolean("Controls/Recal Gyro", false)) {
            Gyro1038.getInstance().calibrate();
            SmartDashboard.putBoolean("Controls/Recal Gyro", false);
        }
    }

    public String getPosition() {
        return position;
    }

    public String getSelectedAuton() {
        return autonChooser;
    }
}
