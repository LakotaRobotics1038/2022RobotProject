package frc.subsystem;

import frc.libraries.*;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.Subsystem;

public class Acquisition implements Subsystem {
    private static Acquisition acquisition;

    public static Acquisition getInstance() {
        if (acquisition == null) {
            System.out.println("Creating a new Acquisition");
            acquisition = new Acquisition();
        }
        return acquisition;
    }

    private Acquisition() {
    }

    // Motor ports *CHANGE THESE OR ROBOT GETS ANGRY!
    // Ports and Constants
    private final int SPINNY_BAR_PORT = 0;
    private final int PUSH_OUT_ACQUISITION_CHANNEL = 0;
    private final int PULL_IN_ACQUISITION_CHANNEL = 0;
    private final static double BEATER_BAR_SPEED = 0.65;

    // States
    private boolean acquisitionIsIn = true;
    public AcquisitionStates acquisitionState = AcquisitionStates.In;
    public boolean XIsPressed = false;

    // Inputs and Outputs
    private final TalonFX1038 spinnyBar = new TalonFX1038(SPINNY_BAR_PORT);
    private DoubleSolenoid acquisitionSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM,
            PUSH_OUT_ACQUISITION_CHANNEL, PULL_IN_ACQUISITION_CHANNEL);

    public enum AcquisitionStates {
        In, Out
    }

    public double motor1Encoder = spinnyBar.getPosition();
    // Motor Speeds

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
        if ((acquisitionIsIn)
                && ((acquisitionSolenoid.get() != Value.kForward) || (acquisitionSolenoid.get() != Value.kReverse))) {
            acquisitionState = AcquisitionStates.Out;
            acquisitionIsIn = false;

            if ((acquisitionIsIn) && ((acquisitionSolenoid.get() != Value.kForward)
                    || (acquisitionSolenoid.get() != Value.kReverse))) {
                acquisitionState = AcquisitionStates.Out;
                acquisitionIsIn = false;

            }

            else if ((!acquisitionIsIn) && ((acquisitionSolenoid.get() != Value.kForward)
                    || (acquisitionSolenoid.get() != Value.kReverse))) {
                acquisitionState = AcquisitionStates.In;
                acquisitionIsIn = true;
            }

        }

        else if ((!acquisitionIsIn)
                && ((acquisitionSolenoid.get() != Value.kForward) || (acquisitionSolenoid.get() != Value.kReverse)))

        {
            acquisitionState = AcquisitionStates.In;
            acquisitionIsIn = true;
        }

    }

    public void runspinnyBarFwd() {
        // If motor is not moving
        spinnyBar.set(BEATER_BAR_SPEED);
    }

    public void runspinnyBarRev() {
        spinnyBar.set(-BEATER_BAR_SPEED);
    }

    public void stopspinnyBar() {
        spinnyBar.set(0);
    }
}
