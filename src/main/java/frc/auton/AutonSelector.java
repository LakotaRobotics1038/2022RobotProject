package frc.auton;

import java.io.IOException;
import java.nio.file.Path;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.auton.commands.AcquireCommand;
import frc.auton.commands.AimCommand;
import frc.auton.commands.ShootCommand;
import frc.libraries.Dashboard;

public class AutonSelector {
    // Path Options
    public static final String ForwardAuto = "ForwardAuto";
    public static final String AcquireAndShoot = "AcquireAndShoot";
    public static final String DriveStraight = "DriveStraight";
    public static final String DriveAndShoot = "DriveAndShoot";
    public static final String DriveShootandAcquire = "DriveShootandAcquire";
    public static final String None = "None";
    public static Boolean TheTryWorked = false;

    // Path Locations
    private final String trajectoryJSON = "frc/auton/Pathweaver/AcquireAndShoot.wpilib.json";

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

    public SequentialCommandGroup chooseAuton() {
        String position = Dashboard.getInstance().getSelectedAuton();
        String autonChooser = Dashboard.getInstance().getPosition();
        Trajectory trajectory = new Trajectory();

        System.out.println("pos: " + position);
        System.out.println("auto: " + autonChooser);

        switch (autonChooser) {
            case AcquireAndShoot:
                Path trajectoryPath = Filesystem.getDeployDirectory().toPath().resolve(trajectoryJSON);
                try {
                    trajectory = TrajectoryUtil.fromPathweaverJson(trajectoryPath);
                    new SequentialCommandGroup(new AcquireCommand(9.0), new AimCommand(9), new ShootCommand(9));
                } catch (IOException ex) {
                    DriverStation.reportError("Unable to open trajectory: " + trajectoryJSON,
                            ex.getStackTrace());
                }

            case DriveAndShoot:
                return new SequentialCommandGroup(new ForwardAuton(), new ShootingAuton());
            case DriveShootandAcquire:
                return new SequentialCommandGroup(new ForwardAuton(), new AcquireCommand(3), new ShootingAuton());

            case None:

            default:
                return new ForwardAuton();
        }

    }
}