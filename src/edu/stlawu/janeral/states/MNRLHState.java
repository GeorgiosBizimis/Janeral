package edu.stlawu.janeral.states;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.stlawu.janeral.err.EnableError;
import edu.stlawu.janeral.err.PortDefError;
import edu.stlawu.janeral.mnrl.*;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

public class MNRLHState extends MNRLNode {

    public boolean isLatched() {
        return latched;
    }

    public MNRLOutputSymbols.StringSymbols getOutputSymbols() {
        return outputSymbols;
    }

    public void setLatched(boolean latched) {
        this.latched = latched;
    }

    public void setOutputSymbols(final MNRLOutputSymbols.StringSymbols outputSymbols) {
        this.outputSymbols = outputSymbols;
    }

    private boolean latched;

    private MNRLOutputSymbols.StringSymbols outputSymbols;

    public MNRLHState(final MNRLOutputSymbols.StringSymbols outputSymbols,
                      final int enable,
                      final String id,
                      final boolean report,
                      final int reportId,
                      final boolean latched,
                      final MNRLAttributes attributes)
            throws EnableError, PortDefError {

        super(id, enable, report,
                List.of(new AbstractMap.SimpleEntry<>(MNRLDefs.H_STATE_INPUT, 1)),
                List.of(new AbstractMap.SimpleEntry<>(MNRLDefs.H_STATE_OUTPUT, 1)),
                attributes);

        this.latched = latched;
        this.reportId = reportId;
        this.outputSymbols = new MNRLOutputSymbols().copyFrom(outputSymbols);
    }

    public MNRLHState(final MNRLOutputSymbols.StringSymbols outputSymbols,
                      final int enable,
                      final String id,
                      final boolean report,
                      final String reportId,
                      final boolean latched,
                      final MNRLAttributes attributes)
            throws EnableError, PortDefError {

        super(id, enable, report,
                List.of(new AbstractMap.SimpleEntry<>(MNRLDefs.H_STATE_INPUT, 1)),
                List.of(new AbstractMap.SimpleEntry<>(MNRLDefs.H_STATE_OUTPUT, 1)),
                attributes);

        this.latched = latched;
        this.reportId = reportId;
        this.outputSymbols = new MNRLOutputSymbols().copyFrom(outputSymbols);
    }

    public MNRLHState(final MNRLOutputSymbols.StringSymbols outputSymbols,
                      final int enable,
                      final String id,
                      final boolean report,
                      final boolean latched,
                      final MNRLAttributes attributes)
            throws EnableError, PortDefError {

        super(id, enable, report,
                List.of(new AbstractMap.SimpleEntry<>(MNRLDefs.H_STATE_INPUT, 1)),
                List.of(new AbstractMap.SimpleEntry<>(MNRLDefs.H_STATE_OUTPUT, 1)),
                attributes);

        this.latched = latched;
        this.reportId = "";
        this.outputSymbols = new MNRLOutputSymbols().copyFrom(outputSymbols);
    }

    public MNRLHState(final MNRLHState other) {
        super(other);
        this.latched = other.isLatched();
        this.reportId = other.getReportId();
        this.outputSymbols = new MNRLOutputSymbols().copyFrom(other.outputSymbols);
    }

    @Override
    public Map<String, Object> toJSON() throws JsonProcessingException {
        final Map<String, Object> map = super.toJSON();
        map.put("type", "hState");

        final Map<String, Object> attributesNode = (Map<String, Object>) map.get("attributes");

        if (reportId != null) {
            attributesNode.put("reportId", reportId.toString());
        }

        attributesNode.put("latched", latched);
        attributesNode.put("symbolSet", outputSymbols.getSymbols());

        return map;
    }
}
