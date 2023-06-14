package edu.stlawu.janeral.err;

public class UpCounterModeError extends MNRLError {
    private int mode;

    public UpCounterModeError(int mode) {
        super("invalid mode");
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }
}
