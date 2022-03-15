package frc.libraries;

import edu.wpi.first.wpilibj.DigitalInput;

public class Prox extends DigitalInput {
    public Prox(int port) {
        super(port);
    }

    @Override
    public boolean get() {
        return !super.get();
    }
}