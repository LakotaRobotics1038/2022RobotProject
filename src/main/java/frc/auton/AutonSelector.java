package frc.auton;

import java.io.IOException;
import java.nio.file.Path;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import frc.libraries.Dashboard;

public class AutonSelector {
    // Path Options
    public static final String TestPath = "TestPath";

    // Path Locations
    private final String trajectoryJSON = "frc/auton/Pathweaver/Testpath.wpilib.json";

    // Fields
    private static AutonSelector autonSelector;

    public static AutonSelector getInstance() {
        if (autonSelector == null) {
            System.out.println("Creating new AutonSelector");
            autonSelector = new AutonSelector();
        }
        return autonSelector;
    }

    private AutonSelector() {
    }

    public void chooseAuton() {
        String position = Dashboard.getInstance().getSelectedAuton();
        String autonChooser = Dashboard.getInstance().getPosition();
        Trajectory trajectory = new Trajectory();

        System.out.println("pos: " + position);
        System.out.println("auto: " + autonChooser);

        switch (autonChooser) {
            case TestPath:
                Path trajectoryPath = Filesystem.getDeployDirectory().toPath().resolve(trajectoryJSON);
                try {
                    trajectory = TrajectoryUtil.fromPathweaverJson(trajectoryPath);
                } catch (IOException ex) {
                    DriverStation.reportError("Unable to open trajectory: " + trajectoryJSON, ex.getStackTrace());
                }
                break;
        }
    }
}