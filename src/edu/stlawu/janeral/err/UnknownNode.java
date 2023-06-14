package edu.stlawu.janeral.err;

public class UnknownNode extends MNRLError {
    private String id;

    public UnknownNode(String id) {
        super("node not found");
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
