package frc.robot;

import frc.libraries.Joystick1038;
import frc.libraries.Joystick1038.PovPositions;
import frc.subsystem.Acquisition;
import frc.subsystem.Endgame;
import frc.subsystem.Shooter;
import frc.subsystem.Storage;
import frc.subsystem.Storage.ManualStorageModes;

public class Operator {
    private static Operator operator;

    public static Operator getInstance() {
        if (operator == null) {
            System.out.println("Creating a new Operator");
            operator = new Operator();
        }
        return operator;
    }

    private final int OPERATOR_JOYSTICK_PORT = 1;

    public Joystick1038 operatorJoystick = new Joystick1038(OPERATOR_JOYSTICK_PORT);
    private final Acquisition acquisition = Acquisition.getInstance();
    private final Endgame endgame = Endgame.getInstance();
    private final Shooter shooter = Shooter.getInstance();
    private final Storage storage = Storage.getInstance();

    private boolean prevYButtonState = false;

    private Operator() {

    }

    public void periodic() {
        if (operatorJoystick.getYButton() && !prevYButtonState) {
            acquisition.toggleAcqPos();
            prevYButtonState = true;
        } else if (!operatorJoystick.getYButton()) {
            prevYButtonState = false;
        }

        if (operatorJoystick.getRightButton()) {
            acquisition.runFwd();
        } else if (operatorJoystick.getLeftButton()) {
            acquisition.runRev();
        } else {
            acquisition.stop();
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

        if (operatorJoystick.getRightJoystickVertical() > 0) {
            storage.setManualStorage(ManualStorageModes.In);
        } else if (operatorJoystick.getRightJoystickVertical() < 0) {
            storage.setManualStorage(ManualStorageModes.Out);
        } else {
            storage.disableManualStorage();
        }

        if (operatorJoystick.getLeftTrigger() > -1) {
            shooter.executeAimPID();
        }

        if (operatorJoystick.getRightTrigger() > -1) {
            shooter.executeSpeedPID(); // TODO: may need adjusting
            shooter.executeHoodPID();
        }
    }
}
