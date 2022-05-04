package frc.robot;

import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.DriveTrain.GearStates;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.commands.SetDriveGearStateCommand;
import frc.robot.commands.ToggleDriveModeCommand;
import frc.robot.libraries.Joystick1038;

public class DriverJoystick {
    private static DriverJoystick driver;

    public static DriverJoystick getInstance() {
        if (driver == null) {
            System.out.println("Creating a new Driver");
            driver = new DriverJoystick();
        }
        return driver;
    }

    public Joystick1038 driverJoystick = new Joystick1038(0);
    private final DriveTrain driveTrain = DriveTrain.getInstance();

    private double drivePower = 0;
    private double multiplier = .5;

    private DriverJoystick() {
        driveTrain.setDefaultCommand(
                new RunCommand(
                        () -> {
                            switch (driveTrain.currentDriveMode) {
                                case tankDrive:
                                    driveTrain.tankDrive(driverJoystick.getLeftY() * multiplier,
                                            driverJoystick.getRightY() * multiplier);
                                    break;
                                case dualArcadeDrive:
                                    if (driverJoystick.getLeftY() > 0) {
                                        // TODO: What is this extra math for?
                                        drivePower = (driverJoystick.getLeftY() - .1) * (1 / .9);
                                    } else if (driverJoystick.getLeftY() < 0) {
                                        drivePower = (driverJoystick.getLeftY() + .1) * (1 / .9);
                                    } else {
                                        drivePower = 0;
                                    }
                                    driveTrain.arcadeDrive(drivePower * multiplier,
                                            driverJoystick.getRightX() * multiplier);
                                    break;
                                case singleArcadeDrive:
                                    driveTrain.arcadeDrive(driverJoystick.getLeftY() * multiplier,
                                            driverJoystick.getLeftX() * multiplier);
                                    break;
                            }
                        },
                        driveTrain));

        driverJoystick.backButton.whenPressed(new ToggleDriveModeCommand(driveTrain));
        driverJoystick.rightBumper.whenPressed(new SetDriveGearStateCommand(driveTrain, GearStates.High));
        driverJoystick.rightBumper.whenReleased(new SetDriveGearStateCommand(driveTrain, GearStates.Low));
        driverJoystick.rightTrigger
                .and(driverJoystick.rightBumper.negate())
                .whenActive(() -> multiplier = 0.8);
        driverJoystick.rightBumper.whenPressed(() -> multiplier = 1.0);
        driverJoystick.rightBumper
                .and(driverJoystick.rightTrigger)
                .whenInactive(() -> multiplier = 0.6);
    }
}
