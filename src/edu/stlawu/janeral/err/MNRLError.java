package edu.stlawu.janeral.err;

public abstract class MNRLError extends Exception {

    public MNRLError(final String string) {
        System.err.println("MNRL ERROR: " + string);
    }

}








