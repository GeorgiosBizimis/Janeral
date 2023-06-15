package edu.stlawu.janeral.test;

import edu.stlawu.janeral.mnrl.Janeral;
import edu.stlawu.janeral.mnrl.MNRLNetwork;

import java.io.IOException;

public class ReadWriteTest {

    private static String INPUT_FILE_PATH;
    private static String OUTPUT_FILE_PATH;

    public static void main(String[] args) {

        if(args[0].equals("-h"))
            printHelp();
        else {
            if(args.length < 2) {
                System.err.println("ERROR: Both input file and output file paths need to be provided.");
                System.exit(1);
            } else if (args.length > 2) {
                System.err.println("ERROR: Redundant arguments were detected.");
                System.exit(1);
            } else {
                INPUT_FILE_PATH = args[0];
                OUTPUT_FILE_PATH = args[1];
            }
        }

        try {
            final MNRLNetwork m = Janeral.loadMNRL(INPUT_FILE_PATH);
            m.exportToFile(OUTPUT_FILE_PATH.formatted(OUTPUT_FILE_PATH.replace("/", "\\")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void printHelp() {
        System.out.println("""
                Help: This is the ReadWrite testing program for the Janeral API.\s
                Enter the input file path as your first argument and your output file path as your second argument.\s
                Make sure to use full paths and the normal slash '/'.""");
        System.exit(0);
    }

}
