package edu.stlawu.janeral.err;

public class InvalidGatePortCount extends MNRLError {
    private int portCount;

    public InvalidGatePortCount(int portCount) {
        super("gate port count must be a positive integer");
        this.portCount = portCount;
    }

    public int getPortCount() {
        return portCount;
    }
}
