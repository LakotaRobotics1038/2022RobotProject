package frc.subsystem;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Subsystem;

public class Storage implements Subsystem {
    private SerialComs serial = SerialComs.getInstance();
    // Ports and Constants
    private final int SHUTTLE_MOTOR_PORT = 17;
    private final int START_LASER_PORT = 0;
    private final int END_LASER_PORT = 2;
    private final int SHUTTLE_MOTOR_ENCODER_COUNTS = 47;
    private final int ENCODER_OFFSET = 500;
    private final double shuttleMotorSpeed = 1.0;

    // declares storage
    private static Storage storage;

    // Inputs and Outputs
    private CANSparkMax shuttleMotor = new CANSparkMax(SHUTTLE_MOTOR_PORT, MotorType.kBrushless);
    private RelativeEncoder shuttleMotorEncoder = shuttleMotor.getEncoder();
    private int laserStart = serial.storageLaser1;// new DigitalInput(START_LASER_PORT);
    private int laserEnd = serial.storageLaser2;
    private int laserDistance = 12;

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
        // serial.testRead();
        switch (selectedManualStorageMode) {
            case None:
                // If the ball is at the first laser and not the middle laser. Move the ball.
                if (laserStart < laserDistance && laserEnd > laserDistance) {
                    shuttleMotor.set(shuttleMotorSpeed);
                    // If a ball is not at the first laser and its at the middle laser. Stop it.
                } else if (laserStart > laserDistance && laserEnd < laserDistance) {
                    shuttleMotor.stopMotor();
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
                shuttleMotor.stopMotor();
        }
    }
}
