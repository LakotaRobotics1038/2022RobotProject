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
    public int storageLaser1 = -1;
    public int storageLaser2 = -1;

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
        serialPort.enableTermination();
        System.out.println("Created new serial reader");
    }

    /**
     * Updates rpi values and reads rpi serial port
     */
    public void read() {
        int bytesToRead = serialPort.getBytesReceived();

        if (bytesToRead > 1) {
            byte[] out = serialPort.read(bytesToRead);
            String outputString = new String(out, 0, out.length, StandardCharsets.UTF_8);
            String[] outputArray = outputString.replace("\n", "").split(",");
            try {
                storageLaser1 = Integer.parseInt(outputArray[0]);
                storageLaser2 = Integer.parseInt(outputArray[1]);
            } catch (NumberFormatException e) {
                // For safety purposes, if we fail to parse the laser data
                // we set to -1 to ensure storage stops
                storageLaser1 = -1;
                storageLaser2 = -1;
                System.out.println("Failed to parse laser data");
            }
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
