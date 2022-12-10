package frc.robot.libraries;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * Add your docs here.
 */
public class Limelight1038 {
    // Ports and Constants
    private final double actualHeight = 68; // Inches
    private final double limelightAngle = 30; // Degrees
    private final double defaultOffset = 0.0;

    // Inputs and Outputs
    private static NetworkTableInstance tableInstance = NetworkTableInstance.getDefault();

    // States
    private static NetworkTable table = tableInstance.getTable("limelight");
    private NetworkTableEntry tv = table.getEntry("tv");
    private NetworkTableEntry tx = table.getEntry("tx");
    private NetworkTableEntry ty = table.getEntry("ty");
    private double valid_target;
    private double x;
    private double y;

    // Enums
    public enum LEDStates {
        On(0), Off(1);

        private int value;

        private LEDStates(int value) {
            this.value = value;
        }
    };

    // Singleton Setup
    private static Limelight1038 instance;

    public static Limelight1038 getInstance() {
        if (instance == null) {
            System.out.println("Creating limelight");
            try {
                instance = new Limelight1038();
            } catch (NullPointerException e) {
                System.out.println("Failed to create Limelight: " + e);
            }
        }
        return instance;
    }

    private Limelight1038() {
        changeLEDStatus(LEDStates.Off);
    }

    /**
     * Reads limelight values from the network table
     */
    public void read() {
        valid_target = tv.getDouble(defaultOffset);
        x = tx.getDouble(defaultOffset);
        y = ty.getDouble(defaultOffset);
    }

    /**
     * Determines if robot has acquired the target
     *
     * @return if robot has a target
     */
    public boolean canSeeTarget() {
        valid_target = tv.getDouble(defaultOffset);
        return valid_target == 1;
    }

    /**
     * @return How far off center horizontally the robot is
     */
    public double getXOffset() {
        x = tx.getDouble(defaultOffset);
        return x;
    }

    /**
     * @return how far from center vertically the robot is
     */
    public double getYOffset() {
        y = ty.getDouble(defaultOffset);
        return y;
    }

    /**
     * Changes the on/off state of the limelight LEDs
     *
     * @param state state to use for the LEDs
     */
    public void changeLEDStatus(LEDStates state) {
        table.getEntry("ledMode").setDouble(state.value);
    }

    /**
     * @return the distance from the limelight to the target
     */
    public double getTargetDistance() {
        // get distance via trig, limelight angle, hub height - limelight height then
        // trig it out
        double distance = actualHeight
                / Math.abs(Math.tan(((limelightAngle + getYOffset()) * (Math.PI / 180.0))));
        return distance;
    }
}