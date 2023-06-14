package edu.stlawu.janeral.mnrl;

import java.util.*;

public class MNRLAttributes {

    private final HashMap<String, Object> attributes;

    private Object symbolSet;
    private boolean latched;
    private String reportId;
    private String gateType;
    private String mode;
    private int threshold ;

    // Getter and Setter methods
    public Object getSymbolSet() {
        return symbolSet;
    }

    public void setSymbolSet(final List<Map.Entry<String, String>> symbolSet) {
        this.symbolSet = new MNRLOutputSymbols().constructAsListOfPairs(symbolSet);
    }
    public void setSymbolSet(final String symbolSet) {
        this.symbolSet = new MNRLOutputSymbols().constructAsString(symbolSet);
    }
    public void setSymbolSet(final MNRLOutputSymbols.StringSymbols symbolSet) {
        this.symbolSet = symbolSet;
    }
    public void setSymbolSet(final MNRLOutputSymbols.ListSymbols symbolSet) {
        this.symbolSet = symbolSet;
    }

    public boolean isLatched() {
        return latched;
    }

    public void setLatched(final boolean latched) {
        this.latched = latched;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(final String reportId) {
        this.reportId = reportId;
    }
    public void setReportId(final int reportId) {
        this.reportId = String.valueOf(reportId);
    }

    public String getGateType() {
        return gateType;
    }

    public void setGateType(final String gateType) {
        this.gateType = gateType;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(final String mode) {
        this.mode = mode;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(final int threshold) {
        this.threshold = threshold;
    }

    public Object get(final String key) {
        return attributes.get(key);
    }

    public void put(final String key, final Object value) {
        attributes.put(key, value);
    }

    public void putAll(final Map<String, Object> all) {
        attributes.putAll(all);
    }

    public MNRLAttributes(final HashMap<String, Object> map) {
        this.gateType = (String) map.get("gateType");
        this.mode = (String) map.get(("mode"));
        this.reportId = String.valueOf(map.get("reportId"));
        this.symbolSet = map.get("symbolSet");
        this.latched = (boolean) map.getOrDefault("latched", false);
        this.threshold = (int) map.getOrDefault("threshold", 0);

        attributes = map;
    }

    public MNRLAttributes(final MNRLAttributes other) {
        this.gateType = String.valueOf(other.gateType);
        this.mode = String.valueOf(other.mode);
        this.reportId = String.valueOf(other.reportId);
        this.symbolSet = new MNRLOutputSymbols().copyFrom(other.symbolSet);
        this.latched = other.latched;
        this.threshold = other.threshold;

        attributes = new HashMap<>() {{
            put("gateType", gateType);
            put("mode", mode);
            put("reportId", reportId);
            put("symbolSet", symbolSet);
            put("latched", latched);
            put("threshold", threshold);
        }};
    }

    public MNRLAttributes(final String gateType, final String mode, final String reportId, final Object symbolSet,
                          final boolean latched, final int threshold) {
        this.gateType = gateType;
        this.mode = mode;
        this.reportId = reportId;
        this.symbolSet = symbolSet;
        this.latched = latched;
        this.threshold = threshold;

        attributes = new HashMap<>() {{
            put("gateType", gateType);
            put("mode", mode);
            put("reportId", reportId);
            put("symbolSet", symbolSet);
            put("latched", latched);
            put("threshold", threshold);
        }};
    }

    public MNRLAttributes(final String gateType, final String mode, final String reportId, final Object symbolSet,
                          final boolean latched, final int threshold, Map<String, Object> more) {
        this.gateType = gateType;
        this.mode = mode;
        this.reportId = reportId;
        this.symbolSet = symbolSet;
        this.latched = latched;
        this.threshold = threshold;

        attributes = new HashMap<>() {{
            put("gateType", gateType);
            put("mode", mode);
            put("reportId", reportId);
            put("symbolSet", symbolSet);
            put("latched", latched);
            put("threshold", threshold);
        }};

        attributes.putAll(more);
    }

    @Override
    public String toString() {
        final HashMap<String, Object> temp = new HashMap<>(attributes);
        final StringBuilder sb = new StringBuilder();
        sb.append("Symbol Set: ").append(temp.remove("symbolSet").toString()).append("]")
                        .append(", [Latched: ").append(temp.remove("latched").toString()).append("]")
                        .append(", [Report ID: ").append(temp.remove("reportId").toString()).append("]")
                        .append(", [Gate Type: ").append(temp.remove("gateType").toString()).append("]")
                        .append(", [Mode: ").append(temp.remove("mode").toString()).append("]")
                        .append(", [Threshold: ").append(temp.remove("threshold").toString()).append("]");
        attributes.forEach((key, value) -> sb.append(", [").append(key).append(": ").append(value).append("]"));
        return "MNRLAttributes{" + sb + '}';
    }

    public String toPrettyString() {
        final HashMap<String, Object> temp = new HashMap<>(attributes);
        final StringBuilder sb = new StringBuilder();
        sb.append("\n\tSymbol Set: ").append(temp.remove("symbolSet").toString())
                .append(", \n\tLatched: ").append(temp.remove("latched").toString())
                .append(", \n\tReport ID: ").append(temp.remove("reportId").toString())
                .append(", \n\t[Gate Type: ").append(temp.remove("gateType").toString())
                .append(", \n\t[Mode: ").append(temp.remove("mode").toString())
                .append(", \n\tThreshold: ").append(temp.remove("threshold").toString());
        attributes.forEach((key, value) -> sb.append(", \n\t").append(key).append(": ").append(value));
        return "MNRLAttributes{" +
                sb +
                "\n}";
    }
}

