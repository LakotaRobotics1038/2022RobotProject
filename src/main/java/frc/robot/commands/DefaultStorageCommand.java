package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.SerialComs;
import frc.robot.subsystems.Storage;

public class DefaultStorageCommand extends CommandBase {
    // Constants
    private final int LASER_DISTANCE = 20;
    private final double SHUTTLE_MOTOR_SPEED = 0.7;

    // Subsystem Dependencies
    private Storage storage = Storage.getInstance();
    private SerialComs serial = SerialComs.getInstance();

    public DefaultStorageCommand() {
        this.addRequirements(storage);
    }

    @Override
    public void execute() {
        int laserStart = serial.getStorageLaser2Val();
        int laserEnd = serial.getStorageLaser1Val();
        // If the lasers have not been read do not run the auto storage code
        if (laserStart < 0 || laserEnd < 0) {
            storage.stop();
            return;
        }
        // If the ball is at the first laser and not the second laser. Move the ball.
        if (laserStart < LASER_DISTANCE && laserEnd > LASER_DISTANCE) {
            storage.setPower(SHUTTLE_MOTOR_SPEED);
            // If a ball is not at the first laser and its at the second laser. Stop it.
        } else if (laserStart > LASER_DISTANCE) {
            storage.stop();
        }
    }

    @Override
    public void end(boolean interrupted) {
        storage.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
