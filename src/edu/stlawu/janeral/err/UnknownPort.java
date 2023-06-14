package edu.stlawu.janeral.err;

public class UnknownPort extends MNRLError {
    private String id;
    private String port_id;

    public UnknownPort(String id, String port_id) {
        super("port not found");
        this.id = id;
        this.port_id = port_id;
    }

    public String getId() {
        return id;
    }

    public String getPortId() {
        return port_id;
    }
}
