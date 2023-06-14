package edu.stlawu.janeral.err;

public class PortDefError extends MNRLError {
    private String ports;

    public PortDefError(String ports) {
        super(ports + "Def must be an array of tuples");
        this.ports = ports;
    }

    public String getPorts() {
        return ports;
    }
}
