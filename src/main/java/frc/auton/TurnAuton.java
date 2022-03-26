package frc.auton;

import frc.auton.commands.TurnCommand;

public class TurnAuton extends Auton {
    public TurnAuton() {
        super();
        addCommands(new TurnCommand(90));
    }
}