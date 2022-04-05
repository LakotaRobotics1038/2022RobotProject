package frc.auton.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.subsystem.Acquisition;
import frc.subsystem.Acquisition.AcquisitionStates;

public class AcquisitionPositionCommand extends CommandBase {
    private Acquisition acquisition = Acquisition.getInstance();
    private AcquisitionStates state;

    /**
     * Creates a new command to raise or lower the acquisition
     *
     * @param state state the acquisition arms should be in
     */
    public AcquisitionPositionCommand(AcquisitionStates state) {
        this.state = state;
    }

    @Override
    public void execute() {
        if (acquisition.acquisitionState != state) {
            acquisition.toggleAcqPos();
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
