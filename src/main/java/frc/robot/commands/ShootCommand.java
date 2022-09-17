package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Storage;

public class ShootCommand extends CommandBase {
    private Shooter shooter = Shooter.getInstance();
    private Storage storage = Storage.getInstance();

    private double startTime = -1;
    private int secondsToShoot = -1;

    /**
     * Creates a new shoot command that will shoot the ball automatically after the
     * provided seconds
     *
     * @param secondsToShoot number of seconds to wait before shooting
     */
    public ShootCommand(int secondsToShoot) {
        this.secondsToShoot = secondsToShoot;

        this.addRequirements(shooter);
    }

    /**
     * Creates a new shoot command that runs until canceled
     */
    public ShootCommand() {
        this.addRequirements(shooter);
    }

    @Override
    public void initialize() {
        shooter.enable();
    }

    @Override
    public void execute() {
        shooter.setSetpoint(shooter.getSetpoint());
        if (secondsToShoot != -1) {
            shooter.feedBall();
            if (shooter.atSetpoint()) {
                startTime = Timer.getFPGATimestamp();
            }
        }
    }

    @Override
    public void end(boolean interrupted) {
        storage.stop();
        shooter.disable();
    }

    @Override
    public boolean isFinished() {
        if (secondsToShoot == -1) {
            return false;
        }

        return shooter.atSetpoint() &&
                Timer.getFPGATimestamp() + secondsToShoot > startTime;
    }
}
