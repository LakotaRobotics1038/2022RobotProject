package frc.robot;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.InstantCommand;
// import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;

import frc.robot.commands.AcquireCommand;
import frc.robot.commands.AcquisitionPositionCommand;
// import frc.robot.commands.AimTurretCommand;
// import frc.robot.commands.AutomaticHoodCommand;
import frc.robot.commands.ElevatorCommand;
import frc.robot.commands.ManualHoodCommand;
import frc.robot.commands.ManualStorageCommand;
import frc.robot.commands.ShootCommand;
// import frc.robot.commands.ZeroTurretCommand;
import frc.robot.commands.AcquireCommand.Modes;
import frc.robot.commands.ElevatorCommand.ManualElevatorModes;
import frc.robot.commands.ManualHoodCommand.ManualHoodModes;
import frc.robot.commands.ManualStorageCommand.ManualStorageModes;

import frc.robot.libraries.Joystick1038;
import frc.robot.libraries.Joystick1038.PovPositions;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;

public class OperatorJoystick {
    // Ports and Constants
    private final int OPERATOR_JOYSTICK_PORT = 1;

    // Inputs
    public Joystick1038 operatorJoystick = new Joystick1038(OPERATOR_JOYSTICK_PORT);

    // Subsystem Dependencies
    private final Shooter shooter = Shooter.getInstance();
    private final Turret turret = Turret.getInstance();

    // States
    // private boolean useManualHood = true;
    private double shooterSpeed = 0.5;

    // Singleton Setup
    private static OperatorJoystick instance;

    public static OperatorJoystick getInstance() {
        if (instance == null) {
            System.out.println("Creating a new Operator");
            instance = new OperatorJoystick();
        }
        return instance;
    }

    private OperatorJoystick() {
        // Acquisition
        operatorJoystick.yButton.whenPressed(new AcquisitionPositionCommand());
        operatorJoystick.rightBumper.whenHeld(new AcquireCommand(Modes.Acquire));
        operatorJoystick.rightTrigger.whileActiveOnce(new AcquireCommand(Modes.Dispose));

        // Turret
        // operatorJoystick.aButton.whenHeld(new AimTurretCommand());

        // Elevator
        operatorJoystick.bButton.whenHeld(new ElevatorCommand(ManualElevatorModes.Up));
        operatorJoystick.xButton.whenHeld(new ElevatorCommand(ManualElevatorModes.Down));

        // Storage
        new Trigger(() -> operatorJoystick.getLeftY() > 0.5)
                .whileActiveOnce(new ManualStorageCommand(ManualStorageModes.In));
        new Trigger(() -> operatorJoystick.getLeftY() < -0.5)
                .whileActiveOnce(new ManualStorageCommand(ManualStorageModes.Out));

        // Hood
        // new Trigger(() -> operatorJoystick.getPOVPosition() == PovPositions.Left)
        // .whenActive(() -> useManualHood = true);
        // new Trigger(() -> operatorJoystick.getPOVPosition() == PovPositions.Right)
        // .whenActive(() -> useManualHood = false);
        new Trigger(() -> operatorJoystick.getPOVPosition() == PovPositions.Up)
                // .and(new Trigger(() -> useManualHood))
                .whenActive(new ManualHoodCommand(ManualHoodModes.Up));
        new Trigger(() -> operatorJoystick.getPOVPosition() == PovPositions.Down)
                // .and(new Trigger(() -> useManualHood))
                .whenActive(new ManualHoodCommand(ManualHoodModes.Down));

        // Shooter
        // operatorJoystick.leftBumper.and(new Trigger(shooter::atSetpoint))
        // .whenActive(() -> {
        // operatorJoystick.setLeftRumble(1);
        // operatorJoystick.setRightRumble(1);
        // })
        // .whenInactive(() -> {
        // operatorJoystick.setLeftRumble(0);
        // operatorJoystick.setRightRumble(0);
        // });
        // operatorJoystick.leftBumper.and(new Trigger(() -> useManualHood))
        // .whileActiveOnce(new ShootCommand());
        operatorJoystick.leftBumper
                .whileHeld(new RunCommand(() -> shooter.shootManually(shooterSpeed), shooter))
                .whenReleased(new InstantCommand(() -> shooter.shootManually(0), shooter));
        // operatorJoystick.leftBumper.and(new Trigger(() -> !useManualHood))
        // .whileActiveOnce(new ParallelCommandGroup(
        // new ShootCommand(),
        // new AutomaticHoodCommand()));
        operatorJoystick.leftTrigger.and(operatorJoystick.leftBumper)
                .whileActiveContinuous(() -> shooter.feedBall());
        new Trigger(() -> operatorJoystick.getPOVPosition() == PovPositions.Right)
                .whenActive(new InstantCommand(() -> setShooterSpeed(shooterSpeed + 0.05)));
        new Trigger(() -> operatorJoystick.getPOVPosition() == PovPositions.Left)
                .whenActive(new InstantCommand(() -> setShooterSpeed(shooterSpeed - 0.05)));

        // turret.setDefaultCommand(new ZeroTurretCommand());
        turret.setDefaultCommand(new RunCommand(() -> {
            turret.setPower(-operatorJoystick.getRightX());
        }, turret));
    }

    private void setShooterSpeed(double speed) {
        this.shooterSpeed = MathUtil.clamp(speed, 0.3, 1);
    }
}