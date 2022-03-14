package frc.robot;

import frc.libraries.Joystick1038;
import frc.subsystem.Acquisition;
import frc.subsystem.Storage.ManualStorageModes;
import frc.subsystem.*;

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
    private final Endgame endgame = Endgame.getInstance();
    private final Storage storage = Storage.getInstance();
    private final Shooter shooter = Shooter.getInstance();

    private Operator() {

    }

    public void periodic() {
        if (operatorJoystick.getXButton()) {
            acquisition.toggleAcqPos();
        }

        if (operatorJoystick.getRightButton()) {
            acquisition.runspinnyBarRev();
        }

        else if (operatorJoystick.getLeftButton()) {
            acquisition.runspinnyBarFwd();
        }

        if (operatorJoystick.getBButton()) {
            endgame.liftElevator();
        }

        if (operatorJoystick.getRightJoystickHorizontal() > -1) {
            endgame.rotateRight();
        }

        if (operatorJoystick.getRightJoystickHorizontal() < -1) {
            endgame.rotateLeft();
        }

        if (operatorJoystick.getRightJoystickVertical() > -1) {
            storage.enableManualStorage(ManualStorageModes.Forward);
        }

        if (operatorJoystick.getRightJoystickVertical() < -1) {
            storage.enableManualStorage(ManualStorageModes.Reverse);
        }

        if (operatorJoystick.getYButton()) {
            storage.periodic();
        }

        if (operatorJoystick.getLeftTrigger() > -1) {
            shooter.executeAimPID();

        }

        if (operatorJoystick.getRightTrigger() > -1) {
            shooter.executeSpeedPID(); // may need adjusting
            shooter.executeHoodPID();
        }

        if (operatorJoystick.getPOV() == 0) {
            endgame.liftElevator();
        }

        if (operatorJoystick.getPOV() == 180) {
            endgame.lowerElevator();
        }

        if (operatorJoystick.getPOV() == 90) {
            endgame.rotateRight();
        }

        if (operatorJoystick.getPOV() == 270) {
            endgame.rotateLeft();
        }

    }
}
