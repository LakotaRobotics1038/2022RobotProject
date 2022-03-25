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

        if (operatorJoystick.getLeftButton()) {
            acquisition.runFwd();
        } else if (operatorJoystick.getLeftTriggerDigital()) {
            acquisition.runRev();
        } else {
            acquisition.stop();
        }

        if (operatorJoystick.getBButton()) {
            endgame.liftElevator();
        } else if (operatorJoystick.getXButton()) {
            endgame.lowerElevator();
        } else {
            endgame.stopElevator();
        }

        if (operatorJoystick.getRightJoystickHorizontal() <= -.5) {
            endgame.rotateRight();
        }

        if (operatorJoystick.getRightJoystickHorizontal() >= .5) {
            endgame.rotateLeft();
        }

        if (operatorJoystick.getRightJoystickHorizontal() == 0) {
            endgame.stopRotator();
        }

        if (operatorJoystick.getRightButton()) {
            shooter.enable();
            // shooter.executeHoodPID();
        } else {
            shooter.disable();
        }

        if (shooter.isFinished() && operatorJoystick.getRightButton()) {
            operatorJoystick.setLeftRumble(1);
            operatorJoystick.setRightRumble(1);
        } else {
            operatorJoystick.setRightRumble(0);
            operatorJoystick.setLeftRumble(0);
        }

        if (operatorJoystick.getRightTriggerDigital()) {
            shooter.feedBall();
        }

        if (operatorJoystick.getLeftJoystickVertical() > .5) {
            storage.setManualStorage(ManualStorageModes.In);
        } else if (operatorJoystick.getLeftJoystickVertical() < -.5) {
            storage.setManualStorage(ManualStorageModes.Out);
        } else {
            storage.setManualStorage(ManualStorageModes.Stop);
            // storage.disableManualStorage();
        }

        if (operatorJoystick.getAButton()) {
            shooter.findTarget();
        } else {
            shooter.returnToZero();
        }

        if (operatorJoystick.getPOVPosition() == PovPositions.Left) {
            shooter.disableHoodPID();
        } else if (operatorJoystick.getPOVPosition() == PovPositions.Right) {
            shooter.enableHoodPID();
        }

        if (operatorJoystick.getPOVPosition() == PovPositions.Up) {
            shooter.moveHoodManually(0.5);
        } else if (operatorJoystick.getPOVPosition() == PovPositions.Down) {
            shooter.moveHoodManually(-0.5);
        } else {
            shooter.moveHoodManually(0);
        }
    }
}