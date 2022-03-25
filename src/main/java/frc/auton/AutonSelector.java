package frc.auton;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.libraries.Dashboard;

public class AutonSelector {
    // Path Options
    public static final String DriveStraight = "DriveStraight";
    public static final String None = "None";

    public static final String LeftPosition = "L";
    public static final String CenterPosition = "C";
    public static final String RightPosition = "R";

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

        System.out.println("pos: " + position);
        System.out.println("auto: " + autonChooser);

        switch (autonChooser) {
            case DriveStraight:
                return new ForwardAuton();
            default:
            case None:
                return null;
        }

    }
}