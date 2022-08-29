package frc.robot;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.button.Trigger;

import frc.robot.commands.AcquireCommand;
import frc.robot.commands.AcquisitionPositionCommand;
import frc.robot.commands.AimTurretCommand;
import frc.robot.commands.AutomaticHoodCommand;
import frc.robot.commands.ElevatorCommand;
import frc.robot.commands.ManualHoodCommand;
import frc.robot.commands.ManualStorageCommand;
import frc.robot.commands.ShootCommand;
import frc.robot.commands.ZeroTurretCommand;
import frc.robot.commands.AcquireCommand.Modes;
import frc.robot.commands.ElevatorCommand.ManualElevatorModes;
import frc.robot.commands.ManualHoodCommand.ManualHoodModes;
import frc.robot.commands.ManualStorageCommand.ManualStorageModes;

import frc.robot.libraries.Joystick1038;
import frc.robot.libraries.Joystick1038.PovPositions;
import frc.robot.subsystems.Hood;
import frc.robot.subsystems.Shooter;
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
    private final Hood hood = Hood.getInstance();

    private boolean useManualHood = false;

    private OperatorJoystick() {
        // Acquisition
        operatorJoystick.yButton.whenPressed(new AcquisitionPositionCommand());
        operatorJoystick.rightBumper.whenHeld(new AcquireCommand(Modes.Acquire));
        operatorJoystick.rightTrigger.whileActiveOnce(new AcquireCommand(Modes.Dispose));

        // Turret
        operatorJoystick.aButton.whenHeld(new AimTurretCommand());

        // Elevator
        operatorJoystick.bButton.whenHeld(new ElevatorCommand(ManualElevatorModes.Up));
        operatorJoystick.xButton.whenHeld(new ElevatorCommand(ManualElevatorModes.Down));

        // Storage
        new Trigger(() -> operatorJoystick.getLeftY() > 0.5)
                .whileActiveOnce(new ManualStorageCommand(ManualStorageModes.In));
        new Trigger(() -> operatorJoystick.getLeftY() < -0.5)
                .whileActiveOnce(new ManualStorageCommand(ManualStorageModes.Out));

        // Hood
        new Trigger(() -> operatorJoystick.getPOVPosition() == PovPositions.Left)
                .whenActive(() -> useManualHood = true);
        new Trigger(() -> operatorJoystick.getPOVPosition() == PovPositions.Right)
                .whenActive(() -> useManualHood = false);
        new Trigger(() -> operatorJoystick.getPOVPosition() == PovPositions.Up)
                .whenActive(new ManualHoodCommand(ManualHoodModes.Up));
        new Trigger(() -> operatorJoystick.getPOVPosition() == PovPositions.Down)
                .whenActive(new ManualHoodCommand(ManualHoodModes.Down));

        // Shooter
        operatorJoystick.leftBumper.and(new Trigger(shooter::atSetpoint))
                .whenActive(() -> {
                    operatorJoystick.setLeftRumble(1);
                    operatorJoystick.setRightRumble(1);
                })
                .whenInactive(() -> {
                    operatorJoystick.setLeftRumble(0);
                    operatorJoystick.setRightRumble(0);
                });
        operatorJoystick.leftBumper.and(new Trigger(() -> useManualHood))
                .whenActive(new ShootCommand());
        operatorJoystick.leftBumper.and(new Trigger(() -> !useManualHood))
                .whenActive(new ParallelCommandGroup(
                        new ShootCommand(),
                        new AutomaticHoodCommand()));
        operatorJoystick.leftTrigger.and(operatorJoystick.leftBumper)
                .whileActiveContinuous(() -> shooter.feedBall());

        turret.setDefaultCommand(new ZeroTurretCommand());
    }
}