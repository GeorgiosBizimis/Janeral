package edu.stlawu.janeral.err;

public class PortWidthMismatch extends MNRLError {
    private int source;
    private int destination;

    public PortWidthMismatch(final int source, final int destination) {
        super("port widths do not align");
        this.source = source;
        this.destination = destination;
    }

    public int getSource() {
        return source;
    }

    public int getDestination() {
        return destination;
    }
}
