package edu.stlawu.janeral.test;

import edu.stlawu.janeral.mnrl.Janeral;
import edu.stlawu.janeral.mnrl.MNRLNetwork;

import java.io.IOException;

public class ReadWrite {

    public static void main(String[] args) {

        final MNRLNetwork m = Janeral.loadMNRL("edu/stlawu/janeral/resources/output.mnrl");
        try {
            m.exportToFile("P:\\workspace\\Janeral\\src\\edu\\stlawu\\janeral\\resources\\output.mnrl");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
