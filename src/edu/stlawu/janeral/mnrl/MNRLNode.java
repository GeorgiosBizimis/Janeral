package edu.stlawu.janeral.mnrl;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.stlawu.janeral.err.EnableError;
import edu.stlawu.janeral.err.PortDefError;

import java.util.*;

public class MNRLNode {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;

    public int getEnable() {
        return enable;
    }

    public boolean isReport() {
        return report;
    }

    public Map<String, Map.Entry<Integer, ArrayList<MNRLConnection>>> getInputDefs() {
        return inputDefs;
    }

    public Map<String, Map.Entry<Integer, ArrayList<MNRLConnection>>> getOutputDefs() {
        return outputDefs;
    }

    public MNRLAttributes getAttributes() {
        return attributes;
    }

    public void setEnable(final int enable) {
        this.enable = enable;
    }

    public void setReport(final boolean report) {
        this.report = report;
    }

    public void setInputDefs(final Map<String, Map.Entry<Integer, ArrayList<MNRLConnection>>> inputDefs) {
        this.inputDefs = inputDefs;
    }

    public void setOutputDefs(final Map<String, Map.Entry<Integer, ArrayList<MNRLConnection>>> outputDefs) {
        this.outputDefs = outputDefs;
    }

    public void setAttributes(final MNRLAttributes attributes) {
        this.attributes = attributes;
    }

    private int enable;
    private boolean report;
    private Map<String, Map.Entry<Integer, ArrayList<MNRLConnection>>> inputDefs;
    private Map<String, Map.Entry<Integer, ArrayList<MNRLConnection>>> outputDefs;
    private MNRLAttributes attributes;

    public MNRLNode(final String id, final int enable, final boolean report,
                    final List<Map.Entry<String, Object>> inputDefs,
                    final List<Map.Entry<String, Object>> outputDefs,
                    final MNRLAttributes attributes)
            throws EnableError, PortDefError {
        initialize(id, enable, report, inputDefs, outputDefs, attributes);
    }

    protected void initialize(final String id, int enable, boolean report,
                              List<Map.Entry<String, Object>> inputDefs,
                              List<Map.Entry<String, Object>> outputDefs,
                              MNRLAttributes attributes) throws EnableError, PortDefError {
        this.id = id;
        if (!isValidEnable(enable)) {
            throw new EnableError(MNRLDefs.toMNRLEnable(enable));
        }
        this.report = report;
        this.inputDefs = validatePorts(inputDefs, "input");
        this.outputDefs = validatePorts(outputDefs, "output");
        this.attributes = attributes;
    }

    public MNRLNode(final MNRLNode other) {
        this.id = String.valueOf(other.id);
        this.enable = other.enable;
        this.report = other.report;
        this.inputDefs = new HashMap<>(other.inputDefs);
        this.outputDefs = new HashMap<>(other.outputDefs);
        this.attributes = new MNRLAttributes(other.attributes);
    }

    public MNRLNode() {

    }

    public Map<String, Object> toJSON() throws JsonProcessingException {
        // Define the enable string
        final String enableString = MNRLDefs.toMNRLEnable(enable);

        // Properly define input ports (drop the connections)
        final List<Map<String, Object>> inputDefsList = new ArrayList<>();
        for (Map.Entry<String, Map.Entry<Integer, ArrayList<MNRLConnection>>> entry : inputDefs.entrySet()) {
            String portId = entry.getKey();
            int width = entry.getValue().getKey();
            Map<String, Object> inputDef = new HashMap<>();
            inputDef.put("portId", portId);
            inputDef.put("width", width);
            inputDefsList.add(inputDef);
        }

        // Properly define output ports
        final List<Map<String, Object>> outputDefsList = new ArrayList<>();
        for (Map.Entry<String, Map.Entry<Integer, ArrayList<MNRLConnection>>> entry : outputDefs.entrySet()) {
            final String portId = entry.getKey();
            final int width = entry.getValue().getKey();
            final ArrayList<MNRLConnection> connectionList = entry.getValue().getValue();
            final Map<String, Object> outputDef = new HashMap<>();
            outputDef.put("portId", portId);
            outputDef.put("width", width);
            List<Map<String, String>> activate = new ArrayList<>(connectionList.size());
            outputDef.put("activate", activate);
            for(MNRLConnection connection : connectionList)
                activate.add(connection.toMap());
            outputDefsList.add(outputDef);
        }

        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("id", id);
        jsonMap.put("report", report);
        jsonMap.put("enable", enableString);
        jsonMap.put("inputDefs", inputDefsList);
        jsonMap.put("outputDefs", outputDefsList);
        jsonMap.put("attributes", new HashMap<>());

        return jsonMap;
    }

    public Map<String, Map.Entry<Integer, ArrayList<MNRLConnection>>> getOutputConnections() {
        return outputDefs;
    }

    public Map<String, Map.Entry<Integer, ArrayList<MNRLConnection>>> getInputConnections() {
        return inputDefs;
    }

    private Map<String, Map.Entry<Integer, ArrayList<MNRLConnection>>> validatePorts(
            final List<Map.Entry<String, Object>> portDefs, final String inout) throws PortDefError {

        final Map<String, Map.Entry<Integer, ArrayList<MNRLConnection>>> validatedPorts = new HashMap<>();
        try {
            for(Map.Entry<String, Object> entry : portDefs) {
                System.out.println(entry);
                final String portId = entry.getKey();
                final int width = Integer.parseInt(entry.getValue().toString());
                System.out.println(portId + width);
                if (portId != null) {
                    if (validatedPorts.containsKey(portId)) {
                        throw new edu.stlawu.janeral.err.DuplicateIdError(portId);
                    } else {
                        if (width > 0) {
                            validatedPorts.put(portId, new AbstractMap.SimpleEntry<>(width, new ArrayList<>()));
                        } else {
                            throw new edu.stlawu.janeral.err.InvalidPortWidth(width);
                        }
                    }
                } else {
                    throw new edu.stlawu.janeral.err.PortIdError();
                }
            }
        } catch (Exception e) {
            //System.out.println(Arrays.toString(e.getStackTrace()));
            throw new edu.stlawu.janeral.err.PortDefError(inout);
        }
        return validatedPorts;
    }

    private boolean isValidEnable(int enable) {
        if(enable == -1)
            enable = MNRLDefs.ENABLE_ON_ACTIVATE_IN;

        return enable == MNRLDefs.ENABLE_ALWAYS ||
                enable == MNRLDefs.ENABLE_ON_ACTIVATE_IN ||
                enable == MNRLDefs.ENABLE_ON_START_AND_ACTIVATE_IN ||
                enable == MNRLDefs.ENABLE_ON_LAST;
    }
}