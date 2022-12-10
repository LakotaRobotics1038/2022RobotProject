package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.Subsystem;

public class Storage implements Subsystem {
    // Ports and Constants
    private final int SHUTTLE_MOTOR_PORT = 17;

    // Outputs
    private CANSparkMax shuttleMotor = new CANSparkMax(SHUTTLE_MOTOR_PORT, MotorType.kBrushless);

    // Inputs
    private RelativeEncoder shuttleMotorEncoder = shuttleMotor.getEncoder();

    // Singleton Setup
    private static Storage instance;

    public static Storage getInstance() {
        if (instance == null) {
            System.out.println("Creating a new storage");
            instance = new Storage();
        }
        return instance;
    }

    private Storage() {
        shuttleMotor.setInverted(false);
        shuttleMotorEncoder.setPositionConversionFactor(47 / 2);
    }

    /**
     * Stops the storage motor
     */
    public void stop() {
        shuttleMotor.stopMotor();
    }

    /**
     * Sets the power for the storage motor
     *
     * @param power positive power for intake, negative for eject
     */
    public void setPower(double power) {
        power = MathUtil.clamp(power, -1, 1);
        shuttleMotor.set(power);
    }

    /**
     * Feeds balls to the shooter
     *
     * @param power how fast to feed the shooter (always +)
     */
    public void feedShooter(double power) {
        shuttleMotor.set(Math.abs(power));
    }
}
