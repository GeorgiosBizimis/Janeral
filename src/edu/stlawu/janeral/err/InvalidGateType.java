package edu.stlawu.janeral.err;

public class InvalidGateType extends MNRLError {
    private String gateType;

    public InvalidGateType(String gateType) {
        super("unknown gate type");
        this.gateType = gateType;
    }

    public String getGateType() {
        return gateType;
    }
}
