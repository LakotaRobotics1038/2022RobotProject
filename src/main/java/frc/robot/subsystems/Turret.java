package frc.robot.subsystems;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.Subsystem;

import frc.robot.libraries.TalonSRX1038;

public class Turret implements Subsystem {
    // Ports and Constants
    private final int TURRET_MOTOR_PORT = 19;
    private final double SWIVEL_SPEED = 0.35;
    private final int LEFT_STOP = 684000;
    private final int RIGHT_STOP = -LEFT_STOP;

    // Outputs
    public TalonSRX1038 turretMotor = new TalonSRX1038(TURRET_MOTOR_PORT);

    // Enums
    public enum TurretDirections {
        Left, Right
    }

    // States
    private TurretDirections currentDirection = TurretDirections.Left;

    // Singleton Setup
    private static Turret instance;

    public static Turret getInstance() {
        if (instance == null) {
            System.out.println("Creating a new Turret");
            instance = new Turret();
        }
        return instance;
    }

    private Turret() {
        turretMotor.setSelectedSensorPosition(0);
    }

    /**
     * Moves the turret according to currentTurretDirection
     */
    public void move() {
        switch (currentDirection) {
            case Left:
                setPower(SWIVEL_SPEED);
                break;
            case Right:
                setPower(-SWIVEL_SPEED);
                break;
        }
    }

    /**
     * @return The current encoder counts of the turret.
     */
    public double getPosition() {
        return turretMotor.getPosition();
    }

    /**
     * This resets the encoder value back to 0 for the turret encoder.
     */
    public void resetTurretEncoder() {
        turretMotor.setSelectedSensorPosition(0);
    }

    /**
     * Stops the turret from moving.
     */
    public void stop() {
        turretMotor.stopMotor();
    }

    /**
     * Set the turret motor power.
     *
     * @param power
     */
    public void setPower(double power) {
        power = MathUtil.clamp(power, -1, 1);

        if (turretMotor.getPosition() >= LEFT_STOP) {
            currentDirection = TurretDirections.Right;
            turretMotor.set(-Math.abs(power));
        } else if (turretMotor.getPosition() <= RIGHT_STOP) {
            currentDirection = TurretDirections.Left;
            turretMotor.set(Math.abs(power));
        } else {
            turretMotor.set(power);
        }
    }

    /**
     * Returns the current direction the turret is moving.
     *
     * @return The turret Direction.
     */
    public TurretDirections getDirection() {
        return currentDirection;
    }

    /**
     * Sets a new direction for the turret to move
     *
     * @param direction
     */
    public void setDirection(TurretDirections direction) {
        currentDirection = direction;
    }
}