package frc.auton;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.subsystem.Dashboard;

public class AutonSelector {
    // Path Options
    public static final String ForwardAuto = "ForwardAuto";
    public static final String ReverseAuto = "ReverseAuto";
    public static final String OneBallAuto = "OneBallAuto";
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
        String chosenAuto = Dashboard.getInstance().getSelectedAuton();
        String position = Dashboard.getInstance().getPosition();

        System.out.println("pos: " + position);
        System.out.println("auto: " + chosenAuto);

        if (chosenAuto == null) {
            System.out.println("Unable to select auton");
            return null;
        }

        switch (chosenAuto) {
            case ForwardAuto:
                return new ForwardAuton();
            case ReverseAuto:
                return new ReverseAuton();
            case OneBallAuto:
                return new OneBallAuton();
            default:
            case None:
                return null;
        }
    }
}