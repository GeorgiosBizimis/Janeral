package edu.stlawu.janeral.states;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.stlawu.janeral.err.EnableError;
import edu.stlawu.janeral.err.PortDefError;
import edu.stlawu.janeral.err.UpCounterModeError;
import edu.stlawu.janeral.err.UpCounterThresholdError;
import edu.stlawu.janeral.mnrl.MNRLAttributes;
import edu.stlawu.janeral.mnrl.MNRLDefs;
import edu.stlawu.janeral.mnrl.MNRLNode;


import java.lang.constant.Constable;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

public class UpCounter extends MNRLNode {

    public String getMode() {
        return mode;
    }

    public Integer getThreshold() {
        return threshold;
    }

    public void setMode(final String mode) {
        this.mode = mode;
    }

    public void setMode(final int mode) {
        this.mode = MNRLDefs.toMNRLCounterMode(mode);
    }


    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }

    private String mode;
    private Integer threshold;

    public UpCounter(final int threshold, final int mode, final String id, final boolean report,
                     final Constable reportId, final MNRLAttributes attributes)
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

        this.setReportId(reportId);
        this.mode = MNRLDefs.toMNRLCounterMode(mode);
        this.threshold = threshold;
    }

    @Override
    public Map<String, Object> toJSON() throws JsonProcessingException {
        final Map<String, Object> map = super.toJSON();

        map.put("type", "hState");

        final Map<String, Object> attributesNode = (Map<String, Object>) map.get("attributes");

        if (reportId != null) {
            attributesNode.put("reportId", reportId.toString());
        }

        attributesNode.put("mode", mode);
        attributesNode.put("threshold", threshold);

        return map;
    }

}
