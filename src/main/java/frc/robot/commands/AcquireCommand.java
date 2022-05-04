package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Acquisition;
import frc.robot.subsystems.Storage;

public class AcquireCommand extends CommandBase {
    private Acquisition acquisition = Acquisition.getInstance();
    private Storage storage = Storage.getInstance();

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
        this.addRequirements(acquisition, storage);
    }

    @Override
    public void execute() {
        switch (mode) {
            case Acquire:
                acquisition.acquire();
            case Dispose:
                acquisition.dispose();
        }

        if (!timerRunning) {
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
        return timer.get() > acqTime;
    }
}
