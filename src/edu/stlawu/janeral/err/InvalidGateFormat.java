package edu.stlawu.janeral.err;

public class InvalidGateFormat extends MNRLError {
    public InvalidGateFormat() {
        super("gate type must be a string");
    }
}
