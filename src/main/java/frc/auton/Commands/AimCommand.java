package frc.auton.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.Timer;

import frc.libraries.Limelight1038;
import frc.libraries.Limelight1038.LEDStates;
import frc.subsystem.Shooter;
import frc.subsystem.Shooter.TurretDirections;

public class AimCommand extends CommandBase {
    private Shooter shooter = Shooter.getInstance();
    private Limelight1038 limelight = Limelight1038.getInstance();

    private boolean turned = false;
    private final double TURRET_SPEED = 0.2;

    private final int TURRET_90_DEGREES = 39500;

    private final double END_TIME;

    public AimCommand(double endTime) {
        END_TIME = endTime;
    }

    @Override
    public void initialize() {
        shooter.setTurretDirection(TurretDirections.Left);
        limelight.changeLEDStatus(LEDStates.On);
    }

    @Override
    public void execute() {
        shooter.executeSpeedPID();
        shooter.findTarget();
    }

    @Override
    public void end(boolean interuppted) {
        limelight.changeLEDStatus(LEDStates.Off);
    }

    @Override
    public boolean isFinished() {
        return (Timer.getMatchTime() <= END_TIME && shooter.turretOnTarget() && shooter.speedOnTarget());
    }
}