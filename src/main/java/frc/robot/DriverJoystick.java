package frc.robot;

import frc.robot.subsystems.DriveTrain;

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
    private boolean prevBackButtonState = false;

    private DriverJoystick() {

    }

    /**
     * Add this method to teleopPeriod to receive button presses from the driver
     * joystick
     */
    public void periodic() {
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

        if (driverJoystick.getBackButton() && !prevBackButtonState) {
            driveTrain.toggleDriveMode();
            prevBackButtonState = true;
        } else if (prevBackButtonState && !driverJoystick.getBackButton()) {
            prevBackButtonState = false;
        }

        if (driverJoystick.getRightBumper() && driverJoystick.getRightTriggerDigital()) {
            multiplier = 1;
            driveTrain.highGear();
        } else if (driverJoystick.getRightBumper()) {
            multiplier = 1;
            driveTrain.lowGear();
        } else if (driverJoystick.getRightTriggerDigital()) {
            multiplier = .8;
            driveTrain.highGear();
        } else {
            multiplier = .6;
            driveTrain.lowGear();
        }
    }
}
