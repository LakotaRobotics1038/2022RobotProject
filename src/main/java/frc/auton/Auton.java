package frc.auton;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class Auton extends SequentialCommandGroup {

    protected String position;
    /**
     * Creates a new auton with data
     * 
     * @param positionIn The position of the robot on the field
     * @param gameDataIn Game data from FMS
     */
    public Auton(String positionIn) {
        position = positionIn;
    }

    /**
     * Creates a new auton without data
     */
    public Auton() {
       
    }
}
