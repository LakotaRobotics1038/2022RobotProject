package frc.robot;

import frc.libraries.DriveTrain1038;

import frc.libraries.Joystick1038;

public class Driver {
    private static Driver driver;

    private double drivePower = 0;
    public double multiplier = .5;

    public static Driver getInstance() {
        if (driver == null) {
            System.out.println("Creating a new Driver");
            driver = new Driver();
        }
        return driver;
    }

    public Joystick1038 driverJoystick = new Joystick1038(0);
    private final DriveTrain1038 driveTrain = DriveTrain1038.getInstance();

    private Driver() {

    }

    public void periodic() {
        switch (driveTrain.currentDriveMode) {
            case tankDrive:
                driveTrain.tankDrive(driverJoystick.getLeftJoystickVertical() * multiplier,
                        driverJoystick.getRightJoystickVertical() * multiplier);
                break;
            case dualArcadeDrive:
                if (driverJoystick.deadband(driverJoystick.getLeftJoystickVertical()) > 0) {
                    drivePower = (driverJoystick.getLeftJoystickVertical() - .1) * (1 / .9);
                } else if (driverJoystick.deadband(driverJoystick.getLeftJoystickVertical()) < 0) {
                    drivePower = (driverJoystick.getLeftJoystickVertical() + .1) * (1 / .9);
                } else {
                    drivePower = 0;
                }
                driveTrain.arcadeDrive(drivePower * multiplier,
                        driverJoystick.getRightJoystickHorizontal() * multiplier);
                break;
            case singleArcadeDrive:
                driveTrain.arcadeDrive(driverJoystick.getLeftJoystickVertical() * multiplier,
                        driverJoystick.getLeftJoystickHorizontal() * multiplier);
                break;
        }

        if (driverJoystick.getRightButton() && driverJoystick.getRightTriggerDigital()) {
            multiplier = 1;
            driveTrain.highGear();
        } else if (driverJoystick.getRightButton()) {
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
