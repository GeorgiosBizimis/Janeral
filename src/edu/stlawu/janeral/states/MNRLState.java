package edu.stlawu.janeral.states;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.stlawu.janeral.err.EnableError;
import edu.stlawu.janeral.err.PortDefError;
import edu.stlawu.janeral.mnrl.*;

import java.util.*;

public class MNRLState extends MNRLNode {

    public Map<String, String> getSymbolSet() {
        return symbolSet;
    }

    public boolean isLatched() {
        return latched;
    }

    public Map<String, String> getOutputSymbols() {
        return outputSymbols;
    }

    public void setSymbolSet(Map<String, String> symbolSet) {
        this.symbolSet = symbolSet;
    }

    public void setLatched(final boolean latched) {
        this.latched = latched;
    }

    public void setOutputSymbols(Map<String, String> outputSymbols) {
        this.outputSymbols = outputSymbols;
    }

    private Map<String, String> symbolSet;
    private Boolean latched;
    private Map<String, String> outputSymbols;

    public MNRLState(final MNRLOutputSymbols.ListSymbols outputSymbols,
                     final int enable,
                     final String id,
                     final boolean report,
                     final String reportId,
                     final boolean latched,
                     final MNRLAttributes attributes) throws PortDefError, EnableError {
        super();

        symbolSet = new HashMap<>();

        final List<Map.Entry<String, Object>> outputDefs = new ArrayList<>();
        for (Map.Entry<String, String> output : outputSymbols.getSymbols()) {
            final String outputId = output.getKey();
            final String symbols = output.getValue();
            if (outputId != null) {
                symbolSet.put(outputId, symbols);
                outputDefs.add(new AbstractMap.SimpleEntry<>(outputId, 1));
            } else {
                throw new PortDefError("output");
            }
        }

       super.initialize(id, enable, report, Collections.singletonList(new AbstractMap.SimpleEntry<>(MNRLDefs.H_STATE_INPUT, 1)),
                outputDefs, attributes);

        this.setReportId(reportId);
        this.setLatched(latched);
        this.setOutputSymbols(new HashMap<>(symbolSet));
    }

    @Override
    public Map<String, Object> toJSON() throws JsonProcessingException {
        final Map<String, Object> map = super.toJSON();
        map.put("type", "hState");

        final Map<String, Object> attributesNode = (Map<String, Object>) map.get("attributes");

        if (reportId != null) {
            attributesNode.put("reportId", reportId);
        }

        attributesNode.put("latched", latched);
        attributesNode.put("symbolSet", outputSymbols);

        return map;
    }

}

