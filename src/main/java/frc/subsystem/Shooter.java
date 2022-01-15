package frc.subsystem;

import frc.libraries.TalonSRX1038;
import frc.libraries.Limelight1038;

public class Shooter implements Subsystem {

    private int talonMotorPort1 = 0;
    private int talonMotorPort2 = 0;

    public TalonSRX1038 talonMotor1 = new TalonSRX1038(talonMotorPort1);
    public TalonSRX1038 talonMotor2 = new TalonSRX1038(talonMotorPort2);

}