package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Acquisition;
import frc.robot.subsystems.Acquisition.AcquisitionStates;

public class AcquisitionPositionCommand extends CommandBase {
    // Subsystem Dependencies
    private Acquisition acquisition = Acquisition.getInstance();

    // States
    private AcquisitionStates state;

    /**
     * Creates a new command to toggle the acquisition state
     */
    public AcquisitionPositionCommand() {
        this.addRequirements(acquisition);
    }

    /**
     * Creates a new command to raise or lower the acquisition
     *
     * @param state state the acquisition arms should be in
     */
    public AcquisitionPositionCommand(AcquisitionStates state) {
        this.state = state;
        this.addRequirements(acquisition);
    }

    @Override
    public void execute() {
        /*
         * If a new acquisition state was explicitly passed set it,
         * If not toggle the current position
         */
        if (this.state != null) {
            this.acquisition.setAcqPos(state);
        } else {
            switch (acquisition.acquisitionState) {
                case In:
                    this.acquisition.setAcqPos(AcquisitionStates.Out);
                    break;

                case Out:
                    this.acquisition.setAcqPos(AcquisitionStates.In);
                    break;
            }
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
