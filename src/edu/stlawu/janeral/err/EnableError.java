package edu.stlawu.janeral.err;

public class EnableError extends MNRLError {
    private String enableCode;

    public EnableError(final String enableCode) {
        super("unknown enable code " + enableCode);
        this.enableCode = enableCode;
    }

    public String getEnableCode() {
        return enableCode;
    }
}
