package frc.subsystem;

import java.nio.charset.StandardCharsets;
import java.text.Format;
import java.io.BufferedReader;

import edu.wpi.first.hal.util.UncleanStatusException;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SerialPort.WriteBufferMode;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.libraries.Limelight1038;

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
    public int storageLaser1 = 0;
    public double lineFollowerData = 0;
    public int storageLaser2 = 0;
    public int storageLaser3 = 0;
    public int limitSwitch1 = 0;
    public int dio1 = 0;
    public int dio2 = 0;
    public int limitSwitch2 = 0;

    // Objects
    private static SerialPort serialPort;
    private static SerialComs rpiCom;
    private Limelight1038 limelight = Limelight1038.getInstance();

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
                storageLaser1 = Integer.parseInt(rpiDataMap[0]);
                storageLaser2 = Integer.parseInt(rpiDataMap[1]);
                storageLaser3 = Integer.parseInt(rpiDataMap[2]);
                limitSwitch1 = Integer.parseInt(rpiDataMap[3]);
                limitSwitch2 = Integer.parseInt(rpiDataMap[4]);
                dio1 = Integer.parseInt(rpiDataMap[5]);
                dio2 = Integer.parseInt(rpiDataMap[6]);
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
    public int getStorageLaser1Val() {
        return storageLaser1;
    }

    /**
     * The rear laser looking towards the ground
     * 
     * @return Distance to ground from rear bottom laser in cm
     */
    public int getStorageLaser2Val() {
        number = storageLaser2;
        long longNumber = Math.round(number);
        int intNumber = Math.toIntExact(longNumber);
        return intNumber;
    }

    /**
     * The front left laser looking forwards
     * 
     * @return Distance to object from front left in cm
     */
    public int getStorageLaser3Val() {
        return storageLaser3;
    }

    /**
     * The front right laser looking forwards
     * 
     * @return Distance to object from front right in cm
     */
    public int getLimitSwitch1Val() {
        return limitSwitch1;
    }

    /**
     * Position of middle of white tape
     * 
     * @return Middle of white tape as an average
     */
    public double getLimitSwitch2Val() {
        return lineFollowerData;
    }

    /**
     * Accelerometer on the four bar
     * 
     * @return Angle of scoring arm by calculating from vertical and horizontal
     *         forces
     */
    public int getDio1Val() {
        return dio1;
    }

    public int getDio2Val() {
        return dio2;
    }

    /**
     * Accelerometer on the wrist piece
     * 
     * @return Angle of wrist by calculating from vertical and horizontal forces
     */
    public int getAcqAccelerometerVal() {
        return limitSwitch2;
    }

    private byte[] testOut;

    public void testRead() {

        // if (serialPort.getBytesReceived() != 0) {
        // testOut = serialPort.read(1);
        rpiOutput = serialPort.readString();
        // rpiOutput = rpiOutput.format(rpiOutput, StandardCharsets.UTF_8);
        String outputString = String.format(rpiOutput, StandardCharsets.UTF_8);
        try {
            int parsedInt = Integer.parseInt(outputString);

            System.out.println("\n \n Will this work " + parsedInt + "\n \n");
        } catch (NumberFormatException ex) {
            // ex.printStackTrace();
            // System.out.println(ex);
        }

        // }

    }

    public void testSend() {
        double distance = limelight.getYOffset();
        String dString = Double.toString(distance);
        // serialPort.setWriteBufferMode(WriteBufferMode.kFlushOnAccess);
        serialPort.writeString("this is a test");
    }
}
