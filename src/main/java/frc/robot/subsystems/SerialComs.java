package frc.robot.subsystems;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj2.command.Subsystem;

public class SerialComs implements Subsystem {
    // Sensors
    public int storageLaser1 = -1;
    public int storageLaser2 = -1;

    // Objects
    private static SerialPort serialPort;
    private static SerialComs instance;

    public enum RobotStates {
        Enabled("A"),
        Disabled("D"),
        EmergencyStop("E");

        public final String value;

        private RobotStates(String value) {
            this.value = value;
        }
    }

    /**
     * Returns the serial coms instance
     *
     * @return serial coms instance
     */
    public static SerialComs getInstance() {
        if (instance == null) {
            instance = new SerialComs();
        }
        return instance;
    }

    /**
     * Initializes the serial communication
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
     * Updates sensor values from serial communication
     */
    @Override
    public void periodic() {
        int bytesToRead = serialPort.getBytesReceived();

        if (bytesToRead > 1) {
            String outputString = serialPort.readString(bytesToRead);
            System.out.println(outputString);
            String[] outputArray = outputString.replace("\n", "").split(",");
            try {
                storageLaser1 = Integer.parseInt(outputArray[0]);
                storageLaser2 = Integer.parseInt(outputArray[1]);
            } catch (NumberFormatException e) {
                // For safety purposes, if we fail to parse the laser data
                // we set to -1 to ensure storage stops
                storageLaser1 = -1;
                storageLaser2 = -1;
                System.out.println("Failed to parse laser data: NaN");
            } catch (IndexOutOfBoundsException e) {
                storageLaser1 = -1;
                storageLaser2 = -1;
                System.out.println("Failed to parse laser data: OoB");
            }
        }
    }

    /**
     * Use the state of the robot to send a command to the LEDs
     *
     * @param state Current state of the robot
     */
    public void setRobotState(RobotStates state) {
        serialPort.writeString(state.value);
    }

    /**
     * Closes serial port listener
     */
    public void stopSerialPort() {
        System.out.println("im gonna close it");
        serialPort.close();
    }

    /**
     * The upper laser in storage
     *
     * @return Distance in cm
     */
    public int getStorageLaser1Val() {
        return storageLaser1;
    }

    /**
     * The lower sensor in storage
     *
     * @return Distance in cm
     */
    public int getStorageLaser2Val() {
        return storageLaser2;
    }
}
