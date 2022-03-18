package frc.libraries;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.subsystem.Shooter;

public class Dashboard {
    private static Dashboard dashboard;
    private Shooter shooter = Shooter.getInstance();
    private Limelight1038 limelight = Limelight1038.getInstance();

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
        SmartDashboard.putNumber("Match Time", -1);
        SmartDashboard.putNumber("Shooter Angle", 0);
        SmartDashboard.putBoolean("Prox", false);
        SmartDashboard.putNumber("Shooter speed", .55);
    }

    public void update() {
        SmartDashboard.putNumber("Shooter Angle", shooter.getTurretEncoder());
        SmartDashboard.putBoolean("Limelight Can See Target",
                limelight.canSeeTarget());
    }

    public String getPosition() {
        return position;
    }

    public String getAutonChooser() {
        return autonChooser;
    }
}
