package edu.stlawu.janeral.states;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.stlawu.janeral.err.EnableError;
import edu.stlawu.janeral.err.InvalidGateFormat;
import edu.stlawu.janeral.err.InvalidGatePortCount;
import edu.stlawu.janeral.err.PortDefError;
import edu.stlawu.janeral.mnrl.MNRLAttributes;
import edu.stlawu.janeral.mnrl.MNRLDefs;
import edu.stlawu.janeral.mnrl.MNRLNode;

import java.lang.constant.Constable;
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

    public void setGateType(String gateType) {
        this.gateType = gateType;
    }

    private String gateType;

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

        this.setGateType(gateType);
        this.setReportId(reportId);
    }

    public MNRLBoolean(final String gateType,
                       final int portCount,
                       final String id,
                       final int enable,
                       final boolean report,
                       final Constable reportId,
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

        this.setGateType(gateType);
        this.setReportId(reportId);
    }

    public MNRLBoolean(final MNRLBoolean other) {
        super(other);
        this.setGateType(other.gateType);
        this.setReportId(other.reportId);
    }

    @Override
    public Map<String, Object> toMap() throws JsonProcessingException {
        final Map<String, Object> map = super.toMap();
        map.put("type", "boolean");

        final Map<String, Object> attributesNode = (Map<String, Object>) map.get("attributes");

        if (reportId != null) {
            attributesNode.put("reportId", reportId);
        }

        attributesNode.put("gateType", gateType);

        return map;
    }

}
