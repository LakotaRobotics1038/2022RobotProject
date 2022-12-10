package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Hood;

public class ManualHoodCommand extends CommandBase {
    // Constants
    private final double MANUAL_HOOD_INCREMENT = 0.25;

    // Subsystem Dependencies
    private Hood hood = Hood.getInstance();

    // Enums
    public enum ManualHoodModes {
        Up, Down
    }

    // States
    private ManualHoodModes direction;

    public ManualHoodCommand(ManualHoodModes direction) {
        this.direction = direction;

        this.addRequirements(hood);
    }

    @Override
    public void initialize() {
        hood.enable();
        double setpoint = 0;
        switch (this.direction) {
            case Up:
                setpoint = hood.getSetpoint() + MANUAL_HOOD_INCREMENT;
                break;
            case Down:
                setpoint = hood.getSetpoint() - MANUAL_HOOD_INCREMENT;
                break;
        }
        hood.setSetpoint(setpoint);
    }

    @Override
    public boolean isFinished() {
        return hood.getController().atSetpoint();
    }
}
