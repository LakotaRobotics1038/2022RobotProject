package frc.robot;

import frc.robot.commands.AcquireCommand;
import frc.robot.commands.AcquisitionPositionCommand;
import frc.robot.commands.AcquireCommand.Modes;
import frc.robot.libraries.Joystick1038;
import frc.robot.libraries.Joystick1038.PovPositions;
import frc.robot.subsystems.Endgame;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Storage;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.Storage.ManualStorageModes;

public class OperatorJoystick {
    private static OperatorJoystick instance;

    public static OperatorJoystick getInstance() {
        if (instance == null) {
            System.out.println("Creating a new Operator");
            instance = new OperatorJoystick();
        }
        return instance;
    }

    private final int OPERATOR_JOYSTICK_PORT = 1;

    public Joystick1038 operatorJoystick = new Joystick1038(OPERATOR_JOYSTICK_PORT);
    private final Endgame endgame = Endgame.getInstance();
    private final Shooter shooter = Shooter.getInstance();
    private final Turret turret = Turret.getInstance();
    private final Storage storage = Storage.getInstance();

    private boolean prevUsedLeftJoystick = false;
    private boolean prevLeftTriggerState = false;

    private OperatorJoystick() {
        operatorJoystick.yButton.whenPressed(new AcquisitionPositionCommand());
        operatorJoystick.rightBumper.whileHeld(new AcquireCommand(Modes.Acquire));
        operatorJoystick.rightTrigger.whileActiveContinuous(new AcquireCommand(Modes.Dispose));
    }

    /**
     * Add this method to teleopPeriod to receive button presses from the operator
     * joystick
     */
    public void periodic() {
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
            turret.findTarget();
        } else {
            turret.returnToZero();
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