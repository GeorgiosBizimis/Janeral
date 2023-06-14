package edu.stlawu.janeral.err;

public class InvalidConnection extends MNRLError {

    public InvalidConnection() {
        super("connection endpoint must be a tuple");
    }

}
