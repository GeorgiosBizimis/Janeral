package edu.stlawu.janeral.mnrl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class MNRLConnection {
    private String id;
    private String portId;

    public MNRLConnection(final String id, final String portId) {
        this.id = id;
        this.portId = portId;
    }

    // Getter and Setter methods
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPortId() {
        return portId;
    }

    public void setPortId(String portId) {
        this.portId = portId;
    }

    public Map<String, String> toMap() {
        return Map.of("portId", portId, "id", id);
    }
}
