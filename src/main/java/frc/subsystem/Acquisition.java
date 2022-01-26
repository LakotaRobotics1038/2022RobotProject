package frc.subsystem;

import frc.libraries.*;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsControlModule;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.Subsystem;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Acquisition implements Subsystem{
    private static Acquisition acquisition;

    public static Acquisition getInstance() {
        if (acquisition == null) {
            System.out.println("Creating a new Acquisition");
            acquisition = new Acquisition();
        }
        return acquisition;
    }
    // Motor ports *CHANGE THESE OR ROBOT GETS ANGRY!

    //Ports
    private final int beaterBarPort = 54;
    //Solenoid channels
    private final int PUSH_OUT_ACQUISITION_CHANNEL = 0;
    private final int PULL_IN_ACQUISITION_CHANNEL = 0;
    final CANSpark1038 beaterBar = new CANSpark1038(beaterBarPort, CANSparkMaxLowLevel.MotorType.kBrushless);
    // Initializing the
     private DoubleSolenoid acquisitionSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, PUSH_OUT_ACQUISITION_CHANNEL, PULL_IN_ACQUISITION_CHANNEL);
    private AcquisitionStates acquisitionState = AcquisitionStates.In;
    private enum AcquisitionStates {
        In, Out
    }
    // Encoder
    public RelativeEncoder motor1Encoder = beaterBar.getEncoder();
    //Motor Speeds
    private final static double BEATER_BAR_SPEED = 0.65;

    public void toggleAcquisitionPosition() {
        switch (acquisitionState) {
            case In:
                acquisitionSolenoid.set(Value.kReverse);
                acquisitionState = AcquisitionStates.Out;
                break;

            case Out:
                acquisitionSolenoid.set(Value.kForward);
                acquisitionState = AcquisitionStates.In;
                break;
        }
}
    public void runBeaterBarFwd() {
        beaterBar.set(BEATER_BAR_SPEED);
    }
    
    public void runBeaterBarRev() {
        beaterBar.set(-BEATER_BAR_SPEED);
    }

    public void stopBeaterBar() {
        beaterBar.set(0);
    }
}
