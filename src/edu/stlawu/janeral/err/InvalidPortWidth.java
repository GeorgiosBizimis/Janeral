package edu.stlawu.janeral.err;

public class InvalidPortWidth extends MNRLError {

    public InvalidPortWidth(final int i) {
        super("Invalid Port width (" + i + "); should be an integer above 0.");
    }

}
