package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Hood;

public class ManualHoodCommand extends CommandBase {
    private Hood hood = Hood.getInstance();

    private double setpoint;

    public ManualHoodCommand(double setpoint) {
        this.setpoint = setpoint;

        this.addRequirements(hood);
    }

    @Override
    public void initialize() {
        hood.enable();
        hood.setSetpoint(setpoint);
    }

    @Override
    public boolean isFinished() {
        return hood.getController().atSetpoint();
    }
}
