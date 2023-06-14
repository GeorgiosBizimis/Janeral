package edu.stlawu.janeral.err;

public class UpCounterThresholdError extends MNRLError {
    private int threshold;

    public UpCounterThresholdError(int threshold) {
        super("threshold must be a non-negative integer");
        this.threshold = threshold;
    }

    public int getThreshold() {
        return threshold;
    }
}
