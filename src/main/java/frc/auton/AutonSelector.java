package frc.auton;

import frc.libraries.*;

import java.io.IOException;
import java.nio.file.Path;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonSelector {
    public static final String TestPath = "TestPath";
    // fields
    private String autonChooser;
    private String position;
    private String gameData;
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
        String trajectoryJSON = "frc/auton/Pathweaver/Testpath.wpilib.json";
        Trajectory trajectory = new Trajectory();

        System.out.println("pos:" + position);
        System.out.println("auto:" + autonChooser);
        System.out.println("game data:" + gameData);

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