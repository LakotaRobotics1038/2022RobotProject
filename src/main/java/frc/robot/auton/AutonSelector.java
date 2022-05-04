package frc.robot.auton;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Dashboard;

public class AutonSelector {
    // Path Options
    public static final String ForwardAuto = "ForwardAuto";
    public static final String ReverseAuto = "ReverseAuto";
    public static final String OneBallAuto = "OneBallAuto";
    public static final String TwoBallAuto = "TwoBallAuto";
    public static final String None = "None";

    public static final String LeftPosition = "L";
    public static final String CenterPosition = "C";
    public static final String RightPosition = "R";

    // Fields
    private static AutonSelector instance;

    public static AutonSelector getInstance() {
        if (instance == null) {
            System.out.println("Creating new AutonSelector");
            instance = new AutonSelector();
        }
        return instance;
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
            case TwoBallAuto:
                return new TwoBallAuton();
            default:
            case None:
                return null;
        }
    }
}