package frc.robot;

import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.DriveTrain.GearStates;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.commands.SetDriveGearStateCommand;
import frc.robot.commands.ToggleDriveModeCommand;
import frc.robot.libraries.Joystick1038;

public class DriverJoystick {
    // Ports and Constants
    private final int DRIVER_JOYSTICK_PORT = 0;

    // Inputs
    public Joystick1038 driverJoystick = new Joystick1038(DRIVER_JOYSTICK_PORT);

    // Subsystem Dependencies
    private final DriveTrain driveTrain = DriveTrain.getInstance();

    // Previous Status
    private double prevLeftY = 0;
    private double prevRightX = 0;

    // Singleton Setup
    private static DriverJoystick instance;

    public static DriverJoystick getInstance() {
        if (instance == null) {
            System.out.println("Creating a new Driver");
            instance = new DriverJoystick();
        }
        return instance;
    }

    private DriverJoystick() {
        SlewRateLimiter driveFilter = new SlewRateLimiter(1.0);
        SlewRateLimiter turnFilter = new SlewRateLimiter(1.0);

        driveTrain.setDefaultCommand(
                new RunCommand(
                        () -> {
                            double leftY = driverJoystick.getLeftY();
                            if (leftY == 0 || signChange(leftY, prevLeftY)) {
                                driveFilter.reset(0);
                            }
                            double limitedLeftY = driveFilter.calculate(leftY);

                            double rightX = driverJoystick.getRightX();
                            if (rightX == 0 || signChange(rightX, prevRightX)) {
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

    /**
     * Determines if the two given values are opposite signs
     * (one positive one negative)
     *
     * @param a first value to check sign
     * @param b second value to check sign
     * @return are the provided values different signs
     */
    private boolean signChange(double a, double b) {
        return a > 0 && b < 0 || b > 0 && a < 0;
    }
}
