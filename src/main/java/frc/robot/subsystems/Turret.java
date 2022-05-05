package frc.robot.subsystems;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Subsystem;

import frc.robot.libraries.TalonSRX1038;
import frc.robot.libraries.Limelight1038.LEDStates;
import frc.robot.libraries.Limelight1038;

public class Turret implements Subsystem {
    private static Turret instance;
    private Limelight1038 limelight = Limelight1038.getInstance();
    private static double SWIVEL_SPEED = 0.35;
    private final double TURRET_POWER_MULTIPLIER = 0.5;
    private final static int LEFT_STOP = 684000;
    private final static int RIGHT_STOP = -LEFT_STOP;
    private TurretDirections currentTurretDirection = TurretDirections.Left;

    public enum TurretDirections {
        Left, Right
    }

    public static Turret getInstance() {
        if (instance == null) {
            System.out.println("Creating a new Turret");
            instance = new Turret();
        }
        return instance;
    }

    // Ports and Constants
    private final int TURRET_MOTOR_PORT = 19;

    // Inputs and Outputs
    public TalonSRX1038 turretMotor = new TalonSRX1038(TURRET_MOTOR_PORT);

    // PID Controller Setup
    // Turret
    private final double positionSetpoint = 0.0;
    private final double positionTolerance = 10;
    private final static double turretP = 0.08; // .15
    private final static double turretI = 0.0;
    private final static double turretD = 0.0;
    private PIDController turretPID = new PIDController(turretP, turretI, turretD);

    private Turret() {
        turretPID.setSetpoint(positionSetpoint);
        turretPID.setTolerance(positionTolerance);
        turretPID.disableContinuousInput();
        turretMotor.setSelectedSensorPosition(0);
    }

    /**
     * Use PID and limelight to aim the turret toward the target
     * This should only be called in shooter.periodic
     */
    private void executeAimPID() {
        double power = turretPID.calculate(limelight.getXOffset());
        if (turretMotor.getPosition() >= LEFT_STOP) {
            turretMotor.set(-Math.abs(power * TURRET_POWER_MULTIPLIER));
        } else if (turretMotor.getPosition() <= RIGHT_STOP) {
            turretMotor.set(Math.abs(power * TURRET_POWER_MULTIPLIER));
        } else {
            turretMotor.set(power * TURRET_POWER_MULTIPLIER);
        }
    }

    /**
     * Determine if the turret is on target
     *
     * @return if the turret is on target and limelight can see the target
     */
    public boolean turretOnTarget() {
        return turretPID.atSetpoint() && limelight.canSeeTarget();
    }

    /**
     * Moves the turret according to currentTurretDirection
     */
    private void moveTurret() {
        switch (currentTurretDirection) {
            case Left:
                turretMotor.set(SWIVEL_SPEED);
                break;
            case Right:
                turretMotor.set(-SWIVEL_SPEED);
                break;
        }
    }

    /**
     * @return The current encoder counts of the turret.
     */
    public double getTurretEncoder() {
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
    private void stopTurret() {
        turretMotor.stopMotor();
    }

    /**
     * Returns the current direction the turret is moving.
     *
     * @return The turret Direction.
     */
    public TurretDirections getTurretDirection() {
        return currentTurretDirection;
    }

    /**
     * Returns the hood and turret back to starting position
     */
    public void returnToZero() {
        if (Math.abs(turretMotor.getPosition()) < 50000) {
            stopTurret();
        } else if (turretMotor.getPosition() > 0) {
            currentTurretDirection = TurretDirections.Right;
            moveTurret();
        } else if (turretMotor.getPosition() < 0) {
            currentTurretDirection = TurretDirections.Left;
            moveTurret();
        }
    }

    /**
     * Uses limelight to find target
     */
    public void findTarget() {
        limelight.changeLEDStatus(LEDStates.On);

        if (turretMotor.getPosition() <= RIGHT_STOP) {
            currentTurretDirection = TurretDirections.Left;
            moveTurret();
        } else if (turretMotor.getPosition() >= LEFT_STOP) {
            currentTurretDirection = TurretDirections.Right;
            moveTurret();
        } else if (limelight.canSeeTarget()) {
            executeAimPID();
        } else {
            moveTurret();
        }
    }
}