package frc.subsystem;

import frc.libraries.TalonFX1038;

public class Endgame {
    public static Endgame endgame;

    public static Endgame getInstance() {
        if (endgame == null) {
            System.out.println("Creating a new Dashbard");
            endgame = new Endgame();
        }
        return endgame;
    }

    private Endgame() {
    }

    // Ports and Constants
    private final int ELEVATOR_PORT = 0;
    private final int ROTATOR_PORT = 0;

    // Inputs and Outputs
    private final TalonFX1038 rotator = new TalonFX1038(ROTATOR_PORT);
    private final TalonFX1038 elevator = new TalonFX1038(ROTATOR_PORT);

    // Encoders for the Motors
    public double getElevatorEncoderPosition() {
        return elevator.getPosition();
    }

    public double getRotatorEncodePosition() {
        return rotator.getPosition(); // might need some extra weird math
    }

    public void liftElevator() {

    }

    public void lowerElevator() {

    }

    public void rotateRight() {

    }

    public void rotateLeft() {

    }

}
