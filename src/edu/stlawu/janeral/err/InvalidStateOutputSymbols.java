package edu.stlawu.janeral.err;

public class InvalidStateOutputSymbols extends MNRLError {
    public InvalidStateOutputSymbols() {
        super("state symbols must be a tuple");
    }
}
