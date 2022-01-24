package frc.subsystem;

import frc.libraries.*;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel;

public class Acquisition {
    private static Acquisition acquisition;

    public static Acquisition getInstance() {
        if (acquisition == null) {
            System.out.println("Creating a new Acquisition");
            acquisition = new Acquisition();
        }
        return acquisition;
    }
    // Motor ports *CHANGE THESE OR ROBOT GETS ANGRY!

    // TODO:Change ports
    private final static int MOTOR1_PORT = 0;
    private final static int MOTOR2_PORT = 0;
    private final static int MOTOR3_PORT = 0;
    // Initializing the motors
    final CANSpark1038 motor1 = new CANSpark1038(MOTOR1_PORT, CANSparkMaxLowLevel.MotorType.kBrushless);
    final CANSpark1038 motor2 = new CANSpark1038(MOTOR2_PORT, CANSparkMaxLowLevel.MotorType.kBrushless);
    final CANSpark1038 motor3 = new CANSpark1038(MOTOR3_PORT, CANSparkMaxLowLevel.MotorType.kBrushless);
    // Encoder????
    public RelativeEncoder motor1Encoder = motor1.getEncoder();
    public RelativeEncoder motor2Encoder = motor2.getEncoder();
    public RelativeEncoder motor3Encoder = motor3.getEncoder();
}
