package edu.stlawu.janeral.mnrl;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.stlawu.janeral.err.*;
import edu.stlawu.janeral.states.MNRLBoolean;
import edu.stlawu.janeral.states.MNRLHState;
import edu.stlawu.janeral.states.MNRLState;
import edu.stlawu.janeral.states.UpCounter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class MNRLNetwork {
    private String id;
    private Map<String, MNRLNode> nodes;
    private int nodesAdded;

    public MNRLNetwork(String id) {
        this.id = id;
        this.nodes = new HashMap<>();
        this.nodesAdded = 0;
    }

    public String toJSON() throws JsonProcessingException {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("id", this.id);
        final List<Map<String, Object>> nodesList = new ArrayList<>(nodes.size());
        jsonMap.put("nodes", nodesList);

        for(MNRLNode node : this.nodes.values())
            nodesList.add(node.toJSON());

        return Janeral.mapper.writeValueAsString(jsonMap);
    }

    public void exportToFile(String filename) throws IOException {
        final File file = new File(filename);
        file.createNewFile();

        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(this.toJSON());
        }
    }

    public MNRLNode getNodeById(String id) throws UnknownNode {
        MNRLNode node = this.nodes.get(id);
        if (node == null) {
            throw new edu.stlawu.janeral.err.UnknownNode(id);
        }
        return node;
    }

    public MNRLNode addNode(MNRLNode node) {
        try {
            node.setId(getUniqueNodeId(node.getId()));
        } catch (DuplicateIdError e) {
            throw new RuntimeException(e);
        }
        this.nodes.put(node.getId(), node);
        return node;
    }

    public MNRLState addState(final MNRLOutputSymbols.ListSymbols outputSymbols, final String enable, String id, final boolean report,
                              final String reportId, final boolean latched, final MNRLAttributes attributes) {
        try {
            id = getUniqueNodeId(id);
        } catch (DuplicateIdError e) {
            throw new RuntimeException(e);
        }
        final MNRLState MNRLState;
        try {
            MNRLState = new MNRLState(outputSymbols, MNRLDefs.fromMNRLEnable(enable),
                    id, report, reportId, latched, attributes);
        } catch (PortDefError | EnableError e) {
            throw new RuntimeException(e);
        }
        this.nodes.put(id, MNRLState);

        return MNRLState;
    }

    public MNRLState addState(final List<Map.Entry<String, String>> outputSymbols, final String enable, String id, final boolean report,
                                final String reportId, final boolean latched, final MNRLAttributes attributes) {
        return addState(new MNRLOutputSymbols().constructAsListOfPairs(outputSymbols), enable, id, report, reportId, latched, attributes);
    }

    public MNRLHState addHState(final MNRLOutputSymbols.StringSymbols outputSymbols, final String enable, String id, final boolean report,
                                final String reportId, final boolean latched, final MNRLAttributes attributes) {
        try {
            id = getUniqueNodeId(id);
        } catch (DuplicateIdError e) {
            throw new RuntimeException(e);
        }
        final MNRLHState hState;
        try {
            hState = new MNRLHState(outputSymbols, MNRLDefs.fromMNRLEnable(enable), id,
                    report, reportId, latched, attributes);
        } catch (EnableError | PortDefError e) {
            throw new RuntimeException(e);
        }
        this.nodes.put(id, hState);

        return hState;
    }

    public MNRLHState addHState(final String outputSymbols, final String enable, String id, final boolean report,
                       final String reportId, final boolean latched, final MNRLAttributes attributes) {
        return addHState(new MNRLOutputSymbols().constructAsString(outputSymbols), enable, id, report, reportId, latched, attributes);
    }

    public UpCounter addUpCounter(final int threshold, final String mode, String id, final boolean report,
                                  final String reportId, final MNRLAttributes attributes) {
        try {
            id = getUniqueNodeId(id);
        } catch (DuplicateIdError e) {
            throw new RuntimeException(e);
        }
        final UpCounter upCounter;
        try {
            upCounter = new UpCounter(threshold, MNRLDefs.fromMNRLCounterMode(mode),
                    id, report, reportId, attributes);
        } catch (UpCounterThresholdError | UpCounterModeError | EnableError | PortDefError e) {
            throw new RuntimeException(e);
        }
        this.nodes.put(id, upCounter);

        return upCounter;
    }

    public MNRLBoolean addBoolean(final String booleanType, String id, final boolean report, final String enable,
                                  final String reportId, final MNRLAttributes attributes)
            throws InvalidGateType {
        try {
            id = getUniqueNodeId(id);
        } catch (DuplicateIdError e) {
            throw new RuntimeException(e);
        }
        final int numberOfPorts;
        if (MNRLDefs.BOOLEAN_TYPES.containsKey(booleanType)) {
            numberOfPorts = MNRLDefs.BOOLEAN_TYPES.get(booleanType);
        } else {
            throw new edu.stlawu.janeral.err.InvalidGateType(booleanType);
        }
        final MNRLBoolean booleanNode;
        try {
            booleanNode = new MNRLBoolean(booleanType, numberOfPorts, id, MNRLDefs.fromMNRLEnable(enable),
                    report, reportId, attributes);
        } catch (InvalidGatePortCount | EnableError | PortDefError | InvalidGateFormat e) {
            throw new RuntimeException(e);
        }
        this.nodes.put(id, booleanNode);

        return booleanNode;
    }

    public void addConnection(final Map.Entry<String, String> source, final Map.Entry<String, String> destination)
            throws UnknownPort, UnknownNode, PortWidthMismatch {
        final String sId = source.getKey();
        final String sPort = source.getValue();
        final String dId = destination.getKey();
        final String dPort = destination.getValue();

        final MNRLNode sNode = getNodeById(sId);
        final MNRLNode dNode = getNodeById(dId);

        // Retrieve the output information from the source node
        final Map.Entry<Integer, ArrayList<MNRLConnection>> sOutputInfo = sNode.getOutputConnections().get(sPort);
        if (sOutputInfo == null) {
            throw new edu.stlawu.janeral.err.UnknownPort(sId, sPort);
        }
        final int sOutputWidth = sOutputInfo.getKey();
        final List<MNRLConnection> sOutput = sOutputInfo.getValue();

        // Retrieve the input information from the destination node
        final Map.Entry<Integer, ArrayList<MNRLConnection>> dInputinfo = dNode.getInputConnections().get(dPort);
        if (dInputinfo == null) {
            throw new edu.stlawu.janeral.err.UnknownPort(dId, dPort);
        }
        int dInputWidth = dInputinfo.getKey();
        final List<MNRLConnection> dInput = dInputinfo.getValue();

        // Check if the output and input widths match
        if (sOutputWidth != dInputWidth) {
            throw new edu.stlawu.janeral.err.PortWidthMismatch(sOutputWidth, dInputWidth);
        }

        // Add the connection
        sOutput.add(new MNRLConnection(dId, dPort));
        dInput.add(new MNRLConnection(sId, sPort));
    }

    public void removeConnection(Map.Entry<String, String> source, Map.Entry<String, String> destination)
            throws UnknownNode {
        final String sId = source.getKey();
        final String sPort = source.getValue();
        final String dId = destination.getKey();
        final String dPort = destination.getValue();

        final MNRLNode sNode = getNodeById(sId);
        final MNRLNode dNode = getNodeById(dId);

        final Map.Entry<Integer, ArrayList<MNRLConnection>> sOutputInfo = sNode.getOutputConnections().get(sPort);
        if (sOutputInfo != null) {
            final List<MNRLConnection> sOutput = sOutputInfo.getValue();
            sOutput.remove(new MNRLConnection(dId, dPort));
        }

        final Map.Entry<Integer, ArrayList<MNRLConnection>> dInputInfo = dNode.getInputConnections().get(dPort);
        if (dInputInfo != null) {
            final List<MNRLConnection> d_input = dInputInfo.getValue();
            d_input.remove(new MNRLConnection(sId, sPort));
        }
    }

    private String getUniqueNodeId(String id)
            throws DuplicateIdError {
        if (id == null) {
            id = "_" + nodesAdded;
            nodesAdded++;
        }

        if (nodes.containsKey(id)) {
            throw new edu.stlawu.janeral.err.DuplicateIdError("This edu.stlawu.janeral.mnrl.MNRL id already exists: " + id);
        }

        return id;
    }

    private Map<Object, Object> getConnectionNodeInformation(final Map.Entry<String, String> source,
                                                             final Map.Entry<String, String> destination)
            throws UnknownNode, UnknownPort {
        final String sId = source.getKey();
        final String sPort = source.getValue();
        final String dId = destination.getKey();
        final String dPort = destination.getValue();

        final MNRLNode sNode = getNodeById(sId);
        final MNRLNode dNode = getNodeById(dId);

        final Map.Entry<Integer, ArrayList<MNRLConnection>> sOutputInfo = sNode.getOutputConnections().get(sPort);
        if (sOutputInfo == null) {
            throw new edu.stlawu.janeral.err.UnknownPort(sId, sPort);
        }

        final int sOutputWidth = sOutputInfo.getKey();
        final List<MNRLConnection> sOutput = sOutputInfo.getValue();

        final Map.Entry<Integer, ArrayList<MNRLConnection>> dInputInfo = dNode.getInputConnections().get(dPort);
        if (dInputInfo == null) {
            throw new edu.stlawu.janeral.err.UnknownPort(dId, dPort);
        }

        final int dInputWidth = dInputInfo.getKey();
        final List<MNRLConnection> dInput = dInputInfo.getValue();

        return Map.of(sId, sPort, sNode, sOutputWidth, sOutput, dId, dPort, dNode, dInputWidth, dInput);
    }
}
