package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDSubsystem;

public class Hood extends PIDSubsystem {
    // Ports and Constants
    private final int HOOD_MOTOR_PORT = 12;

    // Outputs
    private CANSparkMax hoodMotor = new CANSparkMax(HOOD_MOTOR_PORT, MotorType.kBrushless);

    // Inputs
    private RelativeEncoder hoodMotorEncoder = hoodMotor.getEncoder();

    // PID Controller Setup
    private final double HOOD_MAX_DISTANCE = 5.5; // inches
    private final double HOOD_MAX_ENCODER = 88;
    private final double HOOD_ENCODER_COUNTS_PER_INCH = HOOD_MAX_ENCODER / HOOD_MAX_DISTANCE;
    private final double TOLERANCE = .25;
    private final static double P = 0.65;
    private final static double I = 0.03;
    private final static double D = 0.0;

    // Singleton Setup
    private static Hood instance;

    public static Hood getInstance() {
        if (instance == null) {
            System.out.println("Creating a new Hood");
            instance = new Hood();
        }
        return instance;
    }

    private Hood() {
        super(new PIDController(P, I, D));
        getController().setTolerance(TOLERANCE);
        getController().disableContinuousInput();

        hoodMotor.setInverted(true);
        hoodMotorEncoder.setPositionConversionFactor(1 / HOOD_ENCODER_COUNTS_PER_INCH);
        hoodMotorEncoder.setPosition(0);
    }

    /**
     * Adjusts the height of the hood based on distance to target
     */
    @Override
    protected void useOutput(double output, double setpoint) {
        double power = MathUtil.clamp(output, -1, 1);
        hoodMotor.set(power);
    }

    @Override
    protected double getMeasurement() {
        return getHoodEncoder();
    }

    @Override
    public void setSetpoint(double setpoint) {
        setpoint = MathUtil.clamp(setpoint, 0, HOOD_MAX_DISTANCE);
        super.setSetpoint(setpoint);
    }

    /**
     * @return Hood encoder count in inches.
     */
    public double getHoodEncoder() {
        return hoodMotorEncoder.getPosition();
    }
}