package frc.robot;

import frc.robot.libraries.Joystick1038;
import frc.robot.libraries.Joystick1038.PovPositions;
import frc.robot.subsystems.Acquisition;
import frc.robot.subsystems.Endgame;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Storage;
import frc.robot.subsystems.Storage.ManualStorageModes;

public class OperatorJoystick {
    private static OperatorJoystick operator;

    public static OperatorJoystick getInstance() {
        if (operator == null) {
            System.out.println("Creating a new Operator");
            operator = new OperatorJoystick();
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
    private boolean prevUsedLeftJoystick = false;
    private boolean prevLeftTriggerState = false;

    private OperatorJoystick() {

    }

    /**
     * Add this method to teleopPeriod to receive button presses from the operator
     * joystick
     */
    public void periodic() {
        if (operatorJoystick.getYButton() && !prevYButtonState) {
            acquisition.toggleAcqPos();
            prevYButtonState = true;
        } else if (!operatorJoystick.getYButton()) {
            prevYButtonState = false;
        }

        if (operatorJoystick.getRightBumper()) {
            acquisition.acquire();
        } else if (operatorJoystick.getRightTriggerDigital()) {
            acquisition.dispose();
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

        if (operatorJoystick.getLeftBumper()) {
            shooter.enable();
        } else {
            shooter.disable();
        }

        if (operatorJoystick.getLeftTriggerDigital()) {
            shooter.feedBall();
            prevLeftTriggerState = true;
        } else if (prevLeftTriggerState) {
            storage.disableManualStorage();
            prevLeftTriggerState = false;
        }

        if (shooter.isFinished() && operatorJoystick.getRightBumper()) {
            operatorJoystick.setLeftRumble(1);
            operatorJoystick.setRightRumble(1);
        } else {
            operatorJoystick.setRightRumble(0);
            operatorJoystick.setLeftRumble(0);
        }

        if (operatorJoystick.getLeftY() > .5) {
            storage.setManualStorage(ManualStorageModes.In);
            prevUsedLeftJoystick = true;
        } else if (operatorJoystick.getLeftY() < -.5) {
            storage.setManualStorage(ManualStorageModes.Out);
            prevUsedLeftJoystick = true;
        } else if (prevUsedLeftJoystick) {
            storage.disableManualStorage();
            prevUsedLeftJoystick = false;
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