package edu.stlawu.janeral.err;

public class DuplicateIdError extends MNRLError {
    private String id;

    public DuplicateIdError(String id) {
        super(id + " is already defined");
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
