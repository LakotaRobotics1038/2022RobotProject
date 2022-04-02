package frc.subsystem;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.Subsystem;

public class Storage implements Subsystem {
    private SerialComs serial = SerialComs.getInstance();
    // Ports and Constants
    private final int SHUTTLE_MOTOR_PORT = 17;
    private final int SHUTTLE_MOTOR_ENCODER_COUNTS = 47;
    private final int ENCODER_OFFSET = 500;
    private final double shuttleMotorSpeed = 0.7;
    private final int LASER_DISTANCE = 20;

    // declares storage
    private static Storage storage;

    // Inputs and Outputs
    private CANSparkMax shuttleMotor = new CANSparkMax(SHUTTLE_MOTOR_PORT, MotorType.kBrushless);
    private RelativeEncoder shuttleMotorEncoder = shuttleMotor.getEncoder();

    // manual drive
    private ManualStorageModes selectedManualStorageMode = ManualStorageModes.None;

    public enum ManualStorageModes {
        In, Out, None
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
        shuttleMotor.stopMotor();
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
        int laserStart = serial.getStorageLaser2Val();
        int laserEnd = serial.getStorageLaser1Val();
        switch (selectedManualStorageMode) {
            case None:
                // If the lasers have not been read do not run the auto storage code
                if (laserStart < 0 || laserEnd < 0) {
                    shuttleMotor.stopMotor();
                    return;
                }
                // If the ball is at the first laser and not the second laser. Move the ball.
                if (laserStart < LASER_DISTANCE && laserEnd > LASER_DISTANCE) {
                    shuttleMotor.set(shuttleMotorSpeed);
                    // If a ball is not at the first laser and its at the second laser. Stop it.
                } else if (laserStart > LASER_DISTANCE) {
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
        }
    }
}
