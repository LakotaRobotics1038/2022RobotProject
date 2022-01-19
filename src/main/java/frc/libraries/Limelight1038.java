package frc.libraries;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.subsystem.Map1038;
/**
 * Add your docs here.
 */
public class Limelight1038 {
    // LimeLight instance
    private static Limelight1038 limelight;

    // Map instance
    private static Map1038 map = Map1038.getInstance();

    // Network table
    private static NetworkTableInstance tableInstance = NetworkTableInstance.getDefault();
    private static NetworkTable table = tableInstance.getTable("limelight");

    // Network table values
    private NetworkTableEntry tv = table.getEntry("tv");
    private NetworkTableEntry tx = table.getEntry("tx");
    private NetworkTableEntry ty = table.getEntry("ty");

    private double valid_target;
    private double x;
    private double y;

    // Offset default value
    private int defaultOffset = 0;
    private final double MOUNTED_ANGLE = 0;

    public enum LEDStates {
        On(0), Off(1);
        private int value; 
        private LEDStates(int value) { this.value = value; }
    };

    private void Limelight() {
       changeLEDStatus(LEDStates.Off);
    }

    /**
     * returns limelight instance when robot is turned on
     * 
     * @return the limelight instance
     */
    public static Limelight1038 getInstance() {
        if (limelight == null) {
            System.out.println("Creating limelight");
            try {
                limelight = new Limelight1038();
            } catch (NullPointerException e) {
                System.out.println("uh-oh " + e);
            }
        }
        return limelight;
    }

    /**
     * reads the values from the network table
     */
    public void read() {
        valid_target = tv.getDouble(defaultOffset);
        x = tx.getDouble(defaultOffset);
        y = ty.getDouble(defaultOffset);
        // System.out.println(valid_target + ", " + x + ", " + y);
    }

    /**
     * tells if robot has acquired the target
     * 
     * @return whether or not the robot has a target
     */
    public boolean canSeeTarget() {
        valid_target = tv.getDouble(defaultOffset);
        return valid_target == 1;
    }

    /**
     * how far off center horizontally the robot is
     */
    public double getXOffset() {
        x = tx.getDouble(defaultOffset);
        return x;
    }

    /**
     * returns how far from center vertically the robot is
     * @return distance from center vertically
     */
    public double getYOffset() {
        /* if they tell me what to do i wont do it	
         * it ha been 5 minutes since they told me to do something	
         * i am starting to wonder if they are all idiots. 	
         * drew and sam are talking nerd talk	
         * sam is slacking but still talking nerd	
         * i am very bored and want chocy milk	
         * i won the war over the yard stick	
         * they still have not noticed              -Shawn Tomas
         */
        y = ty.getDouble(defaultOffset);
        return y;
    }

    public double limelightDistance() {
        //gets the angle the limelight is at relative to the hub triangle doohicky
        return map.Z_DIFFERENCE / Math.tan(MOUNTED_ANGLE + getYOffset());
    }

    public void changeLEDStatus(LEDStates state) {
        table.getEntry("ledMode").setDouble(state.value);
    }
}
