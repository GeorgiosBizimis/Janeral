package edu.stlawu.janeral.err;

public class DuplicatePortError extends MNRLError {
    private String portId;

    public DuplicatePortError(String portId) {
        super(portId + " is already defined");
        this.portId = portId;
    }

    public String getPortId() {
        return portId;
    }
}
