package frc.subsystem;

import frc.libraries.TalonFX1038;
import frc.libraries.*;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.RelativeEncoder;

public class Endgame {
    public static Endgame endgame;

    public static Endgame getInstance() {
        if (endgame == null) {
            System.out.println("Creating a new Dashbard");
            endgame = new Endgame();
        }
        return endgame;
    }

    // Set Port numbers
    private final int ELEVATOR_PORT = 0;
    private final int ROTATOR_PORT = 0;

    // Initalize objects
    final CANSpark1038 rotator = new CANSpark1038(ELEVATOR_PORT, CANSparkMaxLowLevel.MotorType.kBrushless);
    final RelativeEncoder rotateEncoder = rotator.getEncoder();
    final TalonFX1038 elevator = new TalonFX1038(ROTATOR_PORT);

    // Encoders for the Motors
    public double getElevatorEncoderPosition() {
        return elevator.getPosition();
    }

    public double getRotatorEncodePosition() {
        return rotateEncoder.getPosition(); // might need some extra weird math
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
