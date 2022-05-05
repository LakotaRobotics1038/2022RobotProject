package frc.robot.subsystems;

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
    private static Storage instance;

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
        if (instance == null) {
            System.out.println("Creating a new storage");
            instance = new Storage();
        }
        return instance;
    }

    private Storage() {
        shuttleMotor.setInverted(false);
        shuttleMotorEncoder.setPosition(SHUTTLE_MOTOR_ENCODER_COUNTS + ENCODER_OFFSET);
        shuttleMotorEncoder.setPositionConversionFactor(47 / 2);
    }

    /**
     * Set the mode for manual storage control
     *
     * @param mode the mode manual storage should use.
     *             Do not set ManualStorageModes.None, instead call
     *             disableManualStorage
     */
    public void setManualStorage(ManualStorageModes mode) {
        selectedManualStorageMode = mode;
    }

    /**
     * Sets the manual storage mode to none and stops the shuttle motor.
     * Make sure this is not called in a loop or it will break automatic storage
     */
    public void disableManualStorage() {
        selectedManualStorageMode = ManualStorageModes.None;
        shuttleMotor.stopMotor();
    }

    /**
     * Feeds balls to the shooter
     *
     * @param power how fast to feed the shooter (always +)
     */
    public void feedShooter(double power) {
        shuttleMotor.set(Math.abs(power));
    }

    @Override
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