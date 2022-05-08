package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDSubsystem;

public class Hood extends PIDSubsystem {
    private static Hood instance;

    public static Hood getInstance() {
        if (instance == null) {
            System.out.println("Creating a new Hood");
            instance = new Hood();
        }
        return instance;
    }

    // Ports and Constants
    private final int HOOD_MOTOR_PORT = 12;

    // Inputs and Outputs
    private CANSparkMax hoodMotor = new CANSparkMax(HOOD_MOTOR_PORT, MotorType.kBrushless);
    private RelativeEncoder hoodMotorEncoder = hoodMotor.getEncoder();

    // PID Controller Setup
    // Hood
    private final double HOOD_MAX_DISTANCE = 5.5; // inches
    private final double HOOD_MAX_ENCODER = 88;
    private final double HOOD_ENCODER_COUNTS_PER_INCH = HOOD_MAX_ENCODER / HOOD_MAX_DISTANCE;
    private final double hoodTolerance = .25;
    private final static double hoodP = 0.65;
    private final static double hoodI = 0.03;
    private final static double hoodD = 0.0;
    private PIDController hoodPID;

    private Hood() {
        super(new PIDController(hoodP, hoodI, hoodD));
        hoodPID = getController();
        hoodPID.setTolerance(hoodTolerance);
        hoodPID.disableContinuousInput();
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