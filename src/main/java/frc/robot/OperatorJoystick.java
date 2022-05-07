package frc.robot;

import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.AcquireCommand;
import frc.robot.commands.AcquisitionPositionCommand;
import frc.robot.commands.AimTurretCommand;
import frc.robot.commands.ElevatorCommand;
import frc.robot.commands.ManualStorageCommand;
import frc.robot.commands.ZeroTurretCommand;
import frc.robot.commands.AcquireCommand.Modes;
import frc.robot.commands.ElevatorCommand.ManualElevatorModes;
import frc.robot.commands.ManualStorageCommand.ManualStorageModes;
import frc.robot.libraries.Joystick1038;
import frc.robot.libraries.Joystick1038.PovPositions;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Storage;
import frc.robot.subsystems.Turret;

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
    private final Shooter shooter = Shooter.getInstance();
    private final Turret turret = Turret.getInstance();
    private final Storage storage = Storage.getInstance();

    private boolean prevLeftTriggerState = false;

    private OperatorJoystick() {
        operatorJoystick.yButton.whenPressed(new AcquisitionPositionCommand());
        operatorJoystick.rightBumper.whileHeld(new AcquireCommand(Modes.Acquire));
        operatorJoystick.rightTrigger.whileActiveContinuous(new AcquireCommand(Modes.Dispose));
        operatorJoystick.aButton.whileHeld(new AimTurretCommand());
        operatorJoystick.bButton.whileHeld(new ElevatorCommand(ManualElevatorModes.Up));
        operatorJoystick.xButton.whileHeld(new ElevatorCommand(ManualElevatorModes.Down));
        new Trigger(() -> operatorJoystick.getLeftY() > 0.5)
                .whileActiveContinuous(new ManualStorageCommand(ManualStorageModes.In));
        new Trigger(() -> operatorJoystick.getLeftY() < -0.5)
                .whileActiveContinuous(new ManualStorageCommand(ManualStorageModes.Out));

        turret.setDefaultCommand(new ZeroTurretCommand());
    }

    /**
     * Add this method to teleopPeriod to receive button presses from the operator
     * joystick
     */
    public void periodic() {
        if (operatorJoystick.getLeftBumper()) {
            shooter.enable();
        } else {
            shooter.disable();
        }

        if (operatorJoystick.getLeftTriggerDigital()) {
            shooter.feedBall();
            prevLeftTriggerState = true;
        } else if (prevLeftTriggerState) {
            storage.stop();
            prevLeftTriggerState = false;
        }

        if (shooter.isFinished() && operatorJoystick.getRightBumper()) {
            operatorJoystick.setLeftRumble(1);
            operatorJoystick.setRightRumble(1);
        } else {
            operatorJoystick.setRightRumble(0);
            operatorJoystick.setLeftRumble(0);
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