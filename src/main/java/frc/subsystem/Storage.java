package frc.subsystem;
import edu.wpi.first.wpilibj.Encoder;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.libraries.CANSpark1038;
import frc.libraries.TalonFX1038;

public class Storage implements Subsystem{
    

//Ports
private final int LASER_PORT = 0;
private final int SHUTTLE_MOTOR_PORT = 0;
private final int LASER_MIDDLE_PORT = 0;
private final int LASER_END_PORT = 0;
private final int SHUTTLE_MOTOR_ENCODER_COUNTS = 47;
private final int ENCODER_OFFSET = 500;
private final static double shuttleMotorSpeed = 1.0;


//Laser
private DigitalInput laserStart = new DigitalInput(LASER_PORT);
private DigitalInput laserMiddle = new DigitalInput(LASER_MIDDLE_PORT);
private DigitalInput laserEnd = new DigitalInput(LASER_END_PORT);

//Motor
private TalonFX1038 shuttleMotor = new TalonFX1038(SHUTTLE_MOTOR_PORT);

//Encoder
public double getEncoder() {
    return shuttleMotor.getPosition();
}

//Declares Storage
private static Storage storage;

// manual drive
private boolean manualStorageForward = false;
private boolean manualStorageReverse = false;

public enum ManualStorageModes {
    Forward, Reverse
}

public static Storage getInstance() {
    if (storage == null) {
        System.out.println("creating a new storage");
        storage = new Storage();
    }
    return storage;
}
private Storage() {
    shuttleMotor.setInverted(true);
    shuttleMotor.setPosition(SHUTTLE_MOTOR_ENCODER_COUNTS + ENCODER_OFFSET);
}

public void enableManualStorage(ManualStorageModes mode) {
    switch (mode) {
    case Forward:
        manualStorageForward = true;
        break;
    case Reverse:
        manualStorageReverse = true;
        break;
    default:
        break;
    }
}

public void disableManualStorage() {
    manualStorageReverse = false;
    manualStorageForward = false;
}

/**
 * feeds the shooter
 * 
 * @param power how fast to feed the shooter
 */
public void feedShooter(double power) {
    shuttleMotor.set(power);
}

/**
 * runs the ball storage
 */
public void periodic() {
    if (!manualStorageForward && !manualStorageReverse) {
        //If the ball is at the first laser and not the middle laser. Move the ball.
        if(laserStart.get() && !laserMiddle.get()) {
            shuttleMotor.set(shuttleMotorSpeed);
            //If a ball is not at the first laser and its at the middle laser. Stop it.
            if(!laserStart.get() && laserMiddle.get()) {
                shuttleMotor.set(0);              
            }
        }
        //If there is a ball at the first laser and the middle laser and not the final laser.
        //Move the ball at the middle to the end and the one at the start to the end.
        if(laserStart.get() && laserMiddle.get() && !laserEnd.get()) {
            shuttleMotor.set(shuttleMotorSpeed);
            //if the balls moved to their correct position then stop the belt.
            if(laserEnd.get() && laserMiddle.get() && !laserStart.get()) {
                shuttleMotor.set(0);
            }
        }
        
    } 

    else if (manualStorageForward) {
        shuttleMotor.set(shuttleMotorSpeed);
        shuttleMotor.setPosition(SHUTTLE_MOTOR_ENCODER_COUNTS + ENCODER_OFFSET);
    } 
    
    else if (manualStorageReverse) {
        shuttleMotor.set(-shuttleMotorSpeed);
        shuttleMotor.setPosition(SHUTTLE_MOTOR_ENCODER_COUNTS + ENCODER_OFFSET);
    }
}
}
