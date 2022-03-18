package frc.subsystem;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Subsystem;

public class Storage implements Subsystem {
    // Ports and Constants
    private final int SHUTTLE_MOTOR_PORT = 17;
    private final int START_LASER_PORT = 0;
    private final int MID_LASER_PORT = 1;
    private final int END_LASER_PORT = 2;
    private final int SHUTTLE_MOTOR_ENCODER_COUNTS = 47;
    private final int ENCODER_OFFSET = 500;
    private final double shuttleMotorSpeed = 1.0;

    // declares storage
    private static Storage storage;

    // Inputs and Outputs
    private CANSparkMax shuttleMotor = new CANSparkMax(SHUTTLE_MOTOR_PORT, MotorType.kBrushless);
    private RelativeEncoder shuttleMotorEncoder = shuttleMotor.getEncoder();
    private DigitalInput laserStart = new DigitalInput(START_LASER_PORT);
    private DigitalInput laserMid = new DigitalInput(MID_LASER_PORT);
    private DigitalInput laserEnd = new DigitalInput(END_LASER_PORT);

    // manual drive
    private ManualStorageModes selectedManualStorageMode = ManualStorageModes.None;

    public enum ManualStorageModes {
        In, Out, None, Stop
    }

    /**
     * returns the storage instance when the robot starts
     *
     * @return storage instance
     */
    public static Storage getInstance() {
        if (storage == null) {
            System.out.println("creating a new storage");
            storage = new Storage();
        }
        return storage;
    }

    private Storage() {
        shuttleMotor.setInverted(false);
        shuttleMotorEncoder.setPosition(SHUTTLE_MOTOR_ENCODER_COUNTS + ENCODER_OFFSET);
        shuttleMotorEncoder.setPositionConversionFactor(47 / 2);
    }

    public void setManualStorage(ManualStorageModes mode) {
        selectedManualStorageMode = mode;
    }

    public void disableManualStorage() {
        selectedManualStorageMode = ManualStorageModes.None;
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
        switch (selectedManualStorageMode) {
            case None:
                // If the ball is at the first laser and not the middle laser. Move the ball.
                if (laserStart.get() && !laserMid.get()) {
                    shuttleMotor.set(shuttleMotorSpeed);
                    // If a ball is not at the first laser and its at the middle laser. Stop it.
                } else if (!laserStart.get() && laserMid.get()) {
                    shuttleMotor.set(0);
                }
                // If there is a ball at the first laser and the middle laser and not the
                // laser.
                // Move the ball at the middle to the end and the one at the start to the
                if (laserStart.get() && laserMid.get() && !laserEnd.get()) {
                    shuttleMotor.set(shuttleMotorSpeed);
                    // if the balls moved to their correct position then stop the belt.
                } else if (!laserStart.get() && laserMid.get() && laserEnd.get()) {
                    shuttleMotor.set(0);
                }
                break;
            case In:
                shuttleMotor.set(shuttleMotorSpeed);
                shuttleMotorEncoder.setPosition(SHUTTLE_MOTOR_ENCODER_COUNTS + ENCODER_OFFSET);
                break;
            case Out:
                shuttleMotor.set(-shuttleMotorSpeed);
                shuttleMotorEncoder.setPosition(SHUTTLE_MOTOR_ENCODER_COUNTS + ENCODER_OFFSET);
                break;
            case Stop:
                shuttleMotor.set(0);
        }
    }
}
