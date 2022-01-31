package frc.subsystem;

import java.io.BufferedReader;
import edu.wpi.first.hal.util.UncleanStatusException;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj2.command.Subsystem;

public class SerialComs implements Subsystem {
    // Variables
    private String rpiOutput;
    public String rpiDataMap[];
    public boolean stringRead = false;
    public BufferedReader bufferedReader;
    private static String inputBuffer = "";
    private String line;
    private double number = 0;

    // Sensors
    public int frontLaserSensorData = 0;
    public double lineFollowerData = 0;
    public int rearLaserSensorData = 0;
    public int frontLeftLaserSensorData = 0;
    public int frontRightLaserSensorData = 0;
    public int scoringAccelerometerData = 0;
    public int acquisitionAccelerometerData = 0;

    // Objects
    private static SerialPort serialPort;
    private static SerialComs rpiCom;

    /**
     * Returns the rpi instance created when the robot starts
     * 
     * @return rpi instance
     */
    public static SerialComs getInstance() {
        if (rpiCom == null) {
            rpiCom = new SerialComs();
        }
        return rpiCom;
    }

    /**
     * Initializes the rpi reader (empty currently)
     */
    private SerialComs() {

    }

    /**
     * Creates serial port listener
     */
    public void initialize() {
        serialPort = new SerialPort(9600, SerialPort.Port.kMXP);
        System.out.println("Created new rpi reader");
    }

    /**
     * Updates rpi values and reads rpi serial port
     */
    public void readrpi() {
        try {
            stringRead = false;
            if (serialPort.getBytesReceived() != 0) {
                rpiOutput = serialPort.readString();
                inputBuffer = inputBuffer + rpiOutput;
                stringRead = true;
            }
            line = "";
            if (inputBuffer.indexOf("\r") != -1) {
                int point = inputBuffer.indexOf("\r");
                line = inputBuffer.substring(0, point);
                if (inputBuffer.length() > point + 1) {
                    inputBuffer = inputBuffer.substring(point + 2, inputBuffer.length());
                } else {
                    inputBuffer = "";
                }
            }
            if (line != "") {
                rpiDataMap = line.split(",");
                frontLaserSensorData = Integer.parseInt(rpiDataMap[0]);
                rearLaserSensorData = Integer.parseInt(rpiDataMap[1]);
                frontLeftLaserSensorData = Integer.parseInt(rpiDataMap[2]);
                frontRightLaserSensorData = Integer.parseInt(rpiDataMap[3]);
                acquisitionAccelerometerData = Integer.parseInt(rpiDataMap[4]);
                scoringAccelerometerData = Integer.parseInt(rpiDataMap[5]);
            }
        } catch (NumberFormatException e2) {
        } catch (UncleanStatusException e) {
        }
    }

    /**
     * Closes serial port listener
     */
    public void stopSerialPort() {
        System.out.println("im gonna close it");
        serialPort.close();
    }

    /**
     * The front laser looking towards the ground
     * 
     * @return Distance to ground from front bottom laser in cm
     */
    public int getFrontBottomLaserVal() {
        return frontLaserSensorData;
    }

    /**
     * The rear laser looking towards the ground
     * 
     * @return Distance to ground from rear bottom laser in cm
     */
    public int getRearBottomLaserVal() {
        number = rearLaserSensorData;
        long longNumber = Math.round(number);
        int intNumber = Math.toIntExact(longNumber);
        return intNumber;
    }

    /**
     * The front left laser looking forwards
     * 
     * @return Distance to object from front left in cm
     */
    public int getFrontLeftLaserVal() {
        return frontLeftLaserSensorData;
    }

    /**
     * The front right laser looking forwards
     * 
     * @return Distance to object from front right in cm
     */
    public int getFrontRightLaserVal() {
        return frontRightLaserSensorData;
    }

    /**
     * Position of middle of white tape
     * 
     * @return Middle of white tape as an average
     */
    public double getLineFollowerVal() {
        return lineFollowerData;
    }

    /**
     * Accelerometer on the four bar
     * 
     * @return Angle of scoring arm by calculating from vertical and horizontal
     *         forces
     */
    public int getScoringAccelerometerVal() {
        return scoringAccelerometerData;
    }

    /**
     * Accelerometer on the wrist piece
     * 
     * @return Angle of wrist by calculating from vertical and horizontal forces
     */
    public int getAcqAccelerometerVal() {
        return acquisitionAccelerometerData;
    }

    public void testRead() {

        // if (serialPort.getBytesReceived() != 0) {
        rpiOutput = serialPort.readString();
        System.out.println("\n\n\n" + rpiOutput + "\n\n\n");

        // }

    }

}