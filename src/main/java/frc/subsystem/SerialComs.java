package frc.subsystem;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;

import edu.wpi.first.hal.util.UncleanStatusException;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj2.command.Subsystem;

public class SerialComs implements Subsystem {
    // Variables
    private String rpiOutput;
    public boolean stringRead = false;
    public BufferedReader bufferedReader;
    private static String inputBuffer = "";
    private String line;
    private double number = 0;

    // Sensors
    public int storageLaser1 = 0;
    public int storageLaser2 = 0;

    // Objects
    private static SerialPort serialPort;
    private static SerialComs rpiCom;
    // private Limelight1038 limelight = Limelight1038.getInstance();

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
        this.initialize();
    }

    /**
     * Creates serial port listener
     */
    public void initialize() {
        serialPort = new SerialPort(9600, SerialPort.Port.kMXP);
        System.out.println("Created new serial reader");
    }

    /**
     * Updates rpi values and reads rpi serial port
     */
    public void read() {
        String rpiOutput = serialPort.readString();
        System.out.println("RAW: " + rpiOutput);
        String outputString = String.format(rpiOutput, StandardCharsets.UTF_8);
        char[] outputArray = outputString.toCharArray();
        System.out.println(outputArray);
    }

    @Deprecated
    public void oldRead() {
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
                String[] dataMap = line.split(",");
                storageLaser1 = Integer.parseInt(dataMap[0]);
                storageLaser2 = Integer.parseInt(dataMap[1]);
            }
        } catch (NumberFormatException e) {
            System.out.println("Serial Number Format Exception" + e);
        } catch (UncleanStatusException e) {
            System.out.println("Serial Unclean Status Exception" + e);
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
}
