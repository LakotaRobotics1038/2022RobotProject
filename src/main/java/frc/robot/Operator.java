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

        if (operatorJoystick.getLeftButton()) {
            shooter.executeSpeedPID();
        } else {
            shooter.disableSpeedPID();
            shooter.shootManually(0);
        }

        if (shooter.isFinished() && operatorJoystick.getLeftButton()) {
            operatorJoystick.setLeftRumble(1);
            operatorJoystick.setRightRumble(1);
        } else {
            operatorJoystick.setRightRumble(0);
            operatorJoystick.setLeftRumble(0);
        }

        if (operatorJoystick.getLeftTrigger() > .5) {
            shooter.feedBall();
        }

        if (operatorJoystick.getLeftJoystickVertical() > .5) {
            storage.setManualStorage(ManualStorageModes.In);
        } else if (operatorJoystick.getLeftJoystickVertical() < -.5) {
            storage.setManualStorage(ManualStorageModes.Out);
        } else {
            storage.setManualStorage(ManualStorageModes.Stop);
            storage.disableManualStorage();
        }

        if (operatorJoystick.getAButton()) {
            shooter.findTarget();
        } else {
            shooter.goToCrashPosition();
        }
    }
}