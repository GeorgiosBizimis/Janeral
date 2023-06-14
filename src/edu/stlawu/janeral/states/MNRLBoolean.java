package edu.stlawu.janeral.states;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.stlawu.janeral.err.EnableError;
import edu.stlawu.janeral.err.InvalidGateFormat;
import edu.stlawu.janeral.err.InvalidGatePortCount;
import edu.stlawu.janeral.err.PortDefError;
import edu.stlawu.janeral.mnrl.Janeral;
import edu.stlawu.janeral.mnrl.MNRLAttributes;
import edu.stlawu.janeral.mnrl.MNRLDefs;
import edu.stlawu.janeral.mnrl.MNRLNode;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MNRLBoolean extends MNRLNode {

    public String getGateType() {
        return gateType;
    }

    public String getReportId() {
        return reportId.toString();
    }

    public void setGateType(String gateType) {
        this.gateType = gateType;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    private String gateType;
    private java.io.Serializable reportId;

    public MNRLBoolean(final String gateType,
                       final int portCount,
                       final String id,
                       final int enable,
                       final boolean report,
                       final String reportId,
                       final MNRLAttributes attributes)
            throws InvalidGatePortCount, EnableError, PortDefError, InvalidGateFormat {

        super(id, enable, report,
                IntStream.range(0, Math.max(portCount, 1))
                        .mapToObj(i -> new AbstractMap.SimpleEntry<>("b" + i, (Object) 1))
                        .collect(Collectors.toList()),
                List.of(new AbstractMap.SimpleEntry<>(MNRLDefs.BOOLEAN_OUTPUT, 1)),
                attributes);

        if (gateType != null) {
            if (portCount <= 0)
                throw new InvalidGatePortCount(portCount);

            final List<Map.Entry<String, Object>> inputDefs = new ArrayList<>(portCount);
            for (int i = 0; i < portCount; i++)
                inputDefs.add(new AbstractMap.SimpleEntry<>("b" + i, 1));

        } else {
            throw new InvalidGateFormat();
        }

        this.gateType = gateType;
        this.reportId = reportId;
    }

    public MNRLBoolean(final String gateType,
                       final int portCount,
                       final String id,
                       final int enable,
                       final boolean report,
                       final int reportId,
                       final MNRLAttributes attributes)
            throws InvalidGatePortCount, EnableError, PortDefError, InvalidGateFormat {

        super(id, enable, report,
                IntStream.range(0, Math.max(portCount, 1))
                        .mapToObj(i -> new AbstractMap.SimpleEntry<>("b" + i, (Object) 1))
                        .collect(Collectors.toList()),
                List.of(new AbstractMap.SimpleEntry<>(MNRLDefs.BOOLEAN_OUTPUT, 1)),
                attributes);

        if (gateType != null) {
            if (portCount <= 0)
                throw new InvalidGatePortCount(portCount);

            final List<Map.Entry<String, Object>> inputDefs = new ArrayList<>(portCount);
            for (int i = 0; i < portCount; i++)
                inputDefs.add(new AbstractMap.SimpleEntry<>("b" + i, 1));

        } else {
            throw new InvalidGateFormat();
        }

        this.gateType = gateType;
        this.reportId = reportId;
    }

    public MNRLBoolean(final MNRLBoolean other) {
        super(other);
        this.gateType = other.gateType;
        this.reportId = other.reportId;
    }

    public Map<String, Object> toJSON() throws JsonProcessingException {
        final ObjectNode j = (ObjectNode) super.toJSON();
        j.put("type", "boolean");
        if (reportId != null) {
            final ObjectNode attributesNode = (ObjectNode) j.get("attributes");
            attributesNode.put("reportId", reportId.toString());
        }
        final ObjectNode attributesNode = (ObjectNode) j.get("attributes");
        attributesNode.put("gateType", gateType);

        return Janeral.mapper.convertValue(j, new TypeReference<>(){});
    }
}
