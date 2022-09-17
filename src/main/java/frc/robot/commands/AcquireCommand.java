package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Acquisition;

public class AcquireCommand extends CommandBase {
    private Acquisition acquisition = Acquisition.getInstance();

    public enum Modes {
        Acquire, Dispose
    };

    private Modes mode;
    private double acqTime;
    private boolean timerRunning = false;
    private Timer timer = new Timer();

    /**
     * Creates a new Acquire Command
     *
     * @param mode    Determines whether the acquisition should acquire or dispose
     * @param acqTime time to run the wheels for
     */
    public AcquireCommand(Modes mode, double acqTime) {
        this.mode = mode;
        this.acqTime = acqTime;
        this.addRequirements(acquisition);
    }

    /**
     * Creates a new Acquire Command
     *
     * @param mode    Determines whether the acquisition should acquire or dispose
     * @param acqTime time to run the wheels for
     */
    public AcquireCommand(Modes mode) {
        this.mode = mode;
        this.addRequirements(acquisition);
    }

    @Override
    public void execute() {
        switch (mode) {
            case Acquire:
                acquisition.acquire();
                break;
            case Dispose:
                acquisition.dispose();
                break;
        }

        if (acqTime != 0 && !timerRunning) {
            timerRunning = true;
            timer.start();
        }
    }

    @Override
    public void end(boolean interrupted) {
        acquisition.stop();
    }

    @Override
    public boolean isFinished() {
        if (acqTime != 0) {
            return timer.get() > acqTime;
        }
        return false;
    }
}
