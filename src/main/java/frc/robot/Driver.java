package frc.robot;

import frc.libraries.DriveTrain1038;
import frc.subsystem.*;

import frc.libraries.Joystick1038;

public class Driver {
    private static Driver driver;

    private double drivePower = 0;
    public double multiplyer = .8;

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
                driveTrain.tankDrive(driverJoystick.getLeftJoystickVertical() * multiplyer,
                        driverJoystick.getRightJoystickVertical() * multiplyer);
                break;
            case dualArcadeDrive:
                if (driverJoystick.deadband(driverJoystick.getLeftJoystickVertical()) > 0) {
                    drivePower = (driverJoystick.getLeftJoystickVertical() - .1) * (1 / .9);
                } else if (driverJoystick.deadband(driverJoystick.getLeftJoystickVertical()) < 0) {
                    drivePower = (driverJoystick.getLeftJoystickVertical() + .1) * (1 / .9);
                } else {
                    drivePower = 0;
                }
                driveTrain.dualArcadeDrive(drivePower * multiplyer,
                        driverJoystick.getRightJoystickHorizontal() * multiplyer);
                break;
            case singleArcadeDrive:
                driveTrain.singleAracadeDrive(driverJoystick.getLeftJoystickVertical() * multiplyer,
                        driverJoystick.getLeftJoystickHorizontal() * multiplyer);
                break;
        }

        if (driverJoystick.getRightButton() && driverJoystick.getRightTrigger() > .5) {
            multiplyer = 1;
            driveTrain.highGear();
        } else if (driverJoystick.getRightButton()) {
            multiplyer = 1;
            driveTrain.lowGear();
        } else if (driverJoystick.getRightTrigger() > .5) {
            multiplyer = .8;
            driveTrain.highGear();
        } else {
            multiplyer = .8;
            driveTrain.lowGear();
        }
    }
}
