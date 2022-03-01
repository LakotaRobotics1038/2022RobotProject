package frc.robot;

import frc.libraries.Joystick1038;
import frc.subsystem.Acquisition;

public class Operator {
    private static Operator operator;

    public static Operator getInstance() {
        if (operator == null) {
            System.out.println("Creating a new Operator");
            operator = new Operator();
        }
        return operator;
    }

    private final int OPERATOR_JOYSTICK_PORT = 0;

    public Joystick1038 operatorJoystick = new Joystick1038(OPERATOR_JOYSTICK_PORT);
    private final Acquisition acquisition = Acquisition.getInstance();

    private Operator() {

    }

    public void periodic() {
        if (operatorJoystick.getXButton()) {
            acquisition.toggleAcqPos();
        }

        if (operatorJoystick.getRightButton()) {
            acquisition.runspinnyBarFwd();
        }

        else if (operatorJoystick.getLeftButton()) {
            acquisition.runspinnyBarRev();
        }

    }
}
