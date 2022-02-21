package frc.robot;

import frc.libraries.Joystick1038;

public class Driver {
    private static Driver driver;

    public static Driver getInstance() {
        if (driver == null) {
            System.out.println("Creating a new Driver");
            driver = new Driver();
        }
        return driver;
    }

    public Joystick1038 driverJoystick = new Joystick1038(0);

    private Driver() {

    }

    public void periodic() {

    }
}
