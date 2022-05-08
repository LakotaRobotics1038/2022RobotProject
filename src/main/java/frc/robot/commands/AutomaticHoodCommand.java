package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.libraries.Limelight1038;
import frc.robot.subsystems.Hood;

public class AutomaticHoodCommand extends CommandBase {
    private Hood hood = Hood.getInstance();
    private Limelight1038 limelight = Limelight1038.getInstance();

    public AutomaticHoodCommand() {
        this.addRequirements(hood);
    }

    @Override
    public void initialize() {
        hood.enable();
    }

    @Override
    public void execute() {
        hood.setSetpoint(limelight.getTargetDistance() / 40);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
