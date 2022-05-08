package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Elevator;

public class ElevatorCommand extends CommandBase {
    private Elevator elevator = Elevator.getInstance();

    private final double RAISE_ELEVATOR_POWER = 0.4;
    private final double LOWER_ELEVATOR_POWER = -0.6;

    private ManualElevatorModes selectedMode;

    public enum ManualElevatorModes {
        Up, Down
    }

    public ElevatorCommand(ManualElevatorModes mode) {
        this.selectedMode = mode;

        this.addRequirements(elevator);
    }

    @Override
    public void execute() {
        switch (selectedMode) {
            case Up:
                elevator.releaseRatchet();
                if (!elevator.ratchetIsEngaged()) {
                    elevator.setPower(RAISE_ELEVATOR_POWER);
                }
                break;
            case Down:
                elevator.engageRatchet();
                elevator.setPower(LOWER_ELEVATOR_POWER);
                break;
        }
    }

    @Override
    public void end(boolean interrupted) {
        elevator.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
