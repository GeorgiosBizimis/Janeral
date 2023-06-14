package edu.stlawu.janeral.states;

import edu.stlawu.janeral.err.EnableError;
import edu.stlawu.janeral.err.PortDefError;
import edu.stlawu.janeral.err.UpCounterModeError;
import edu.stlawu.janeral.err.UpCounterThresholdError;
import edu.stlawu.janeral.mnrl.MNRLAttributes;
import edu.stlawu.janeral.mnrl.MNRLDefs;
import edu.stlawu.janeral.mnrl.MNRLNode;

import java.util.AbstractMap;
import java.util.List;

public class UpCounter extends MNRLNode {
    public UpCounter(final int threshold, final int mode, final String id, final boolean report,
                     final String reportId, final MNRLAttributes attributes)
            throws UpCounterThresholdError, UpCounterModeError, EnableError, PortDefError {
        super(id, MNRLDefs.ENABLE_ON_START_AND_ACTIVATE_IN, report,
                List.of(new AbstractMap.SimpleEntry<>(MNRLDefs.UP_COUNTER_COUNT, 1),
                        new AbstractMap.SimpleEntry<>(MNRLDefs.UP_COUNTER_RESET, 1)),
                List.of(new AbstractMap.SimpleEntry<>(MNRLDefs.UP_COUNTER_OUTPUT, 1)),
                attributes);

        if(threshold < 0)
            throw new UpCounterThresholdError(threshold);

        if(mode != MNRLDefs.TRIGGER_ON_THRESHOLD &&
            mode != MNRLDefs.HIGH_ON_THRESHOLD &&
            mode != MNRLDefs.ROLLOVER_ON_THRESHOLD)
            throw new UpCounterModeError(mode);
    }
}
