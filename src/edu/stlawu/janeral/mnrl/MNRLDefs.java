package edu.stlawu.janeral.mnrl;

import java.util.*;

public class MNRLDefs {
    public static final int ENABLE_ON_ACTIVATE_IN = 0;
    public static final int ENABLE_ON_START_AND_ACTIVATE_IN = 1;
    public static final int ENABLE_ALWAYS = 2;
    public static final int ENABLE_ON_LAST = 3;
    public static final int TRIGGER_ON_THRESHOLD = 4;
    public static final int HIGH_ON_THRESHOLD = 5;
    public static final int ROLLOVER_ON_THRESHOLD = 6;

    public static final String H_STATE_INPUT = "i";
    public static final String STATE_INPUT = "i";
    public static final String H_STATE_OUTPUT = "o";
    public static final String UP_COUNTER_OUTPUT = "o";
    public static final String BOOLEAN_OUTPUT = "o";

    public static final String UP_COUNTER_COUNT = "cnt";
    public static final String UP_COUNTER_RESET = "rst";

    public static final Map<String, Integer> BOOLEAN_TYPES =
            Map.of("and", 1,
                    "or", 1,
                    "nor", 1,
                    "not", 1,
                    "nand", 1);

    public static int fromMNRLEnable(final String enableString) {
        return switch (enableString) {
            case "onActivateIn" -> ENABLE_ON_ACTIVATE_IN;
            case "onStartAndActivateIn" -> ENABLE_ON_START_AND_ACTIVATE_IN;
            case "onLast" -> ENABLE_ON_LAST;
            case "always" -> ENABLE_ALWAYS;
            default -> -1;
        };
    }

    public static String toMNRLEnable(final int enableInt) {
        return switch (enableInt) {
            case ENABLE_ON_ACTIVATE_IN -> "onActivateIn";
            case ENABLE_ON_START_AND_ACTIVATE_IN -> "onStartAndActivateIn";
            case ENABLE_ALWAYS -> "always";
            case ENABLE_ON_LAST -> "onLast";
            default -> "none";
        };
    }

    public static int fromMNRLCounterMode(final String counterString) {
        return switch (counterString) {
            case "trigger" -> TRIGGER_ON_THRESHOLD;
            case "high" -> HIGH_ON_THRESHOLD;
            case "rollover" -> ROLLOVER_ON_THRESHOLD;
            default -> -1;
        };
    }

    public static String toMNRLCounterMode(final int counterInt) {
        return switch (counterInt) {
            case TRIGGER_ON_THRESHOLD -> "trigger";
            case HIGH_ON_THRESHOLD -> "high";
            case ROLLOVER_ON_THRESHOLD -> "rollover";
            default -> "error";
        };
    }

}

