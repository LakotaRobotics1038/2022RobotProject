package frc.subsystem;

import frc.libraries.*;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsControlModule;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.Subsystem;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import frc.robot.Robot;
import frc.libraries.Joystick1038;

public class Acquisition implements Subsystem {
    private static Acquisition acquisition;

    public static Acquisition getInstance() {
        if (acquisition == null) {
            System.out.println("Creating a new Acquisition");
            acquisition = new Acquisition();
        }
        return acquisition;
    }
    // Motor ports *CHANGE THESE OR ROBOT GETS ANGRY!

<<<<<<< HEAD
    //Ports
    private final int spinnyBarPort = 0;
    //Solenoid channels
    private final int PUSH_OUT_ACQUISITION_CHANNEL = 0;
    private final int PULL_IN_ACQUISITION_CHANNEL = 0;
    private boolean AcquisitionIsIn = true;
    final TalonFX1038 spinnyBar = new TalonFX1038(spinnyBarPort);
    private DoubleSolenoid acquisitionSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, PUSH_OUT_ACQUISITION_CHANNEL, PULL_IN_ACQUISITION_CHANNEL);
    public AcquisitionStates acquisitionState = AcquisitionStates.In;
    public boolean XIsPressed = false;
    private final Joystick1038 joystick = Joystick1038.getInstance(0);
=======
    // Ports
    private final int beaterBarPort = 0;
    // Solenoid channels
    private final int PUSH_OUT_ACQUISITION_CHANNEL = 0;
    private final int PULL_IN_ACQUISITION_CHANNEL = 0;
    final CANSpark1038 beaterBar = new CANSpark1038(beaterBarPort, CANSparkMaxLowLevel.MotorType.kBrushless);
    // Initializing the
    private DoubleSolenoid acquisitionSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM,
            PUSH_OUT_ACQUISITION_CHANNEL, PULL_IN_ACQUISITION_CHANNEL);
    public AcquisitionStates acquisitionState = AcquisitionStates.In;
    public boolean XIsPressed = false;

>>>>>>> master
    public enum AcquisitionStates {
        In, Out
    }

    // Encoder
<<<<<<< HEAD
    //github is dumb
    public double motor1Encoder = spinnyBar.getPosition();
    //Motor Speeds
=======
    // github is dumb
    public RelativeEncoder motor1Encoder = beaterBar.getEncoder();
    // Motor Speeds
>>>>>>> master
    private final static double BEATER_BAR_SPEED = 0.65;

    public void periodic() {
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

    public void toggleAcqPos() {
<<<<<<< HEAD
        if((AcquisitionIsIn) && ((acquisitionSolenoid.get() != Value.kForward) || (acquisitionSolenoid.get() != Value.kReverse))) {
            acquisitionState = AcquisitionStates.Out;
            AcquisitionIsIn = false;
            
        }

     else if((!AcquisitionIsIn) && ((acquisitionSolenoid.get() != Value.kForward) || (acquisitionSolenoid.get() != Value.kReverse))) {
            acquisitionState = AcquisitionStates.In;
            AcquisitionIsIn = true;
        }

    }
    public void runspinnyBarFwd() {
        //If motor is not moving
        spinnyBar.set(BEATER_BAR_SPEED);
    }
    
    public void runspinnyBarRev() {
        spinnyBar.set(-BEATER_BAR_SPEED);
=======
        if (XIsPressed) {
            acquisitionState = AcquisitionStates.In;
            XIsPressed = false;
        }

        else {
            acquisitionState = AcquisitionStates.Out;
            XIsPressed = true;
        }

    }

    public void runBeaterBarFwd() {
        beaterBar.set(BEATER_BAR_SPEED);
    }

    public void runBeaterBarRev() {
        beaterBar.set(-BEATER_BAR_SPEED);
>>>>>>> master
    }

    public void stopspinnyBar() {
        spinnyBar.set(0);
    }
}
