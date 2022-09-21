package frc.robot;

import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.DriveTrain.GearStates;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.commands.SetDriveGearStateCommand;
import frc.robot.commands.ToggleDriveModeCommand;
import frc.robot.libraries.Joystick1038;

public class DriverJoystick {
    private static DriverJoystick instance;

    public static DriverJoystick getInstance() {
        if (instance == null) {
            System.out.println("Creating a new Driver");
            instance = new DriverJoystick();
        }
        return instance;
    }

    public Joystick1038 driverJoystick = new Joystick1038(0);
    private final DriveTrain driveTrain = DriveTrain.getInstance();
    private double prevLeftY = 0;
    private double prevRightX = 0;

    private DriverJoystick() {
        SlewRateLimiter driveFilter = new SlewRateLimiter(1.0);
        SlewRateLimiter turnFilter = new SlewRateLimiter(1.0);

        driveTrain.setDefaultCommand(
                new RunCommand(
                        () -> {
                            double leftY = driverJoystick.getLeftY();
                            if (signChange(leftY, prevLeftY)) {
                                driveFilter.reset(0);
                            }
                            double limitedLeftY = driveFilter.calculate(leftY);

                            double rightX = driverJoystick.getRightX();
                            if (signChange(rightX, prevRightX)) {
                                turnFilter.reset(0);
                            }
                            double limitedRightX = turnFilter.calculate(rightX);

                            switch (driveTrain.currentDriveMode) {
                                case tankDrive:
                                    driveTrain.tankDrive(limitedLeftY,
                                            limitedRightX);
                                    break;
                                case dualArcadeDrive:
                                    driveTrain.arcadeDrive(limitedLeftY,
                                            driverJoystick.getRightX());
                                    break;
                                case singleArcadeDrive:
                                    driveTrain.arcadeDrive(limitedLeftY,
                                            driverJoystick.getLeftX());
                                    break;
                            }
                            prevLeftY = leftY;
                            prevRightX = rightX;
                        },
                        driveTrain));

        driverJoystick.backButton.whenPressed(new ToggleDriveModeCommand());
        driverJoystick.rightBumper.whenPressed(new SetDriveGearStateCommand(GearStates.High));
        driverJoystick.rightBumper.whenReleased(new SetDriveGearStateCommand(GearStates.Low));
    }

    private boolean signChange(double a, double b) {
        return a > 0 && b < 0 || b > 0 && a < 0;
    }
}
