package frc.libraries;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.I2C;

public class Gyro1038 extends AnalogGyro {

	// Variables
	public final int SENSOR_ID_CODE = 0x02;
	private final int COMMAND = 0x03;
	private final int HEADING_DATA = 0x04;
	private final int INTEGRATED_Z_VALUE = 0x06;
	private final int RAW_X_VALUE = 0x08;
	private final int RAW_Y_VALUE = 0x0A;
	private final int RAW_Z_VALUE = 0x0C;
	private final int Z_AXIS_OFFSET = 0x0E;
	private final int DEVICE_ADDRESS = 0x10;
	private final int NORMAL_MEASUREMENT_MODE = 0x00;
	private final int GYRO_RECALIBRATE = 0x4E;
	private final int RESET_Z_AXIS_INTEGRATOR = 0x52;
	private double gyroVal;

	// Objects
	private I2C I2CBus;
	private static Gyro1038 gyroSensor;

	/**
	 * Initializes the gyro to listen to the onboard I2C port at the set address and
	 * calibrates the gyro
	 */

	// TODO: Make sure i2c gyro works with analogGyro class

	private Gyro1038() {
		super(0);
		I2CBus = new I2C(I2C.Port.kOnboard, DEVICE_ADDRESS);
		calibrate();
	}

	/**
	 * Returns the gyro instance created when the robot starts
	 * 
	 * @return Gyro instance
	 */
	public static Gyro1038 getInstance() {
		if (gyroSensor == null) {
			System.out.println("Creating a new Gyro");
			gyroSensor = new Gyro1038();
		}
		return gyroSensor;
	}

	@Override
	public double getAngle() {
		readGyro();
		return gyroVal;
	}

	/**
	 * Calculates the heading of the gyro
	 */
	public void readGyro() {
		byte[] dataBuffer = new byte[6];

		if (I2CBus == null) {
			gyroVal = 102.7;
		}
		I2CBus.read(COMMAND, 6, dataBuffer);
		if (dataBuffer[1] >= 0) {
			gyroVal = dataBuffer[1];
		} else {
			gyroVal = 256 + dataBuffer[1];
		}
		if (dataBuffer[2] > 0) {
			gyroVal = 256 + gyroVal;
		}

		while (gyroVal < 0) {
			gyroVal = gyroVal + 360;
		}
		while (gyroVal > 359) {
			gyroVal = gyroVal - 360;
		}
	}

	@Override
	public void reset() {
		I2CBus.write(COMMAND, RESET_Z_AXIS_INTEGRATOR);
		I2CBus.write(SENSOR_ID_CODE, NORMAL_MEASUREMENT_MODE);
	}

	@Override
	public void calibrate() {
		System.out.println("Gyro Calibrated");
		I2CBus.write(COMMAND, GYRO_RECALIBRATE);
		I2CBus.write(SENSOR_ID_CODE, NORMAL_MEASUREMENT_MODE);
	}

	@Override
	/**
	 * This method is not currently implemented
	 */
	public double getRate() {
		return 0;
	}

	@Override
	public void close() {
	}
}
