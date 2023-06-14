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
    private Map<String, String> symbolSet = new HashMap<>();
    private boolean latched;
    private Object reportId;
    private Map<String, String> outputSymbols;

    public MNRLState(final MNRLOutputSymbols.ListSymbols outputSymbols,
                     final int enable,
                     final String id,
                     final boolean report,
                     final String reportId,
                     final boolean latched,
                     final MNRLAttributes attributes) throws PortDefError, EnableError {
        super();

        final List<Map.Entry<String, Object>> outputDefs = new ArrayList<>();
        for (Map.Entry<String, String> output : outputSymbols.getSymbols()) {
            final String outputId = output.getKey();
            final String symbolSet = output.getValue();
            if (outputId != null) {
                this.symbolSet.put(outputId, symbolSet);
                outputDefs.add(new AbstractMap.SimpleEntry<>(outputId, 1));
            } else {
                throw new PortDefError("output");
            }
        }

       super.initialize(id, enable, report, Collections.singletonList(new AbstractMap.SimpleEntry<>(MNRLDefs.H_STATE_INPUT, 1)),
                outputDefs, attributes);

        this.reportId = reportId;
        this.latched = latched;
        this.outputSymbols = new HashMap<>(symbolSet);
    }

    public Map<String, Object> toJSON() throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();

        final ObjectNode j = (ObjectNode) super.toJSON();
        j.put("type", "hState");

        if (reportId != null) {
            final ObjectNode attributesNode = (ObjectNode) j.get("attributes");
            attributesNode.put("reportId", reportId.toString());
        }

        final ObjectNode attributesNode = (ObjectNode) j.get("attributes");
        attributesNode.put("latched", latched);
        attributesNode.put("symbolSet", mapper.writeValueAsString(outputSymbols));

        return Janeral.mapper.convertValue(j, new TypeReference<>(){});
    }

}

