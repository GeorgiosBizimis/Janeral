package edu.stlawu.janeral.mnrl;/*
class edu.stlawu.janeral.mnrl.MNRLDecoder(json.JSONDecoder):
    def decode(self, json_string):
        default_obj = super(edu.stlawu.janeral.mnrl.MNRLDecoder,self).decode(json_string)

        # build up a proper edu.stlawu.janeral.mnrl.MNRL representation
        mnrl_obj = edu.stlawu.janeral.mnrl.MNRLNetwork(default_obj['id'])

        # build up the mnrl network in two passes
        # 1. add all the nodes
        # 2. add all the connections

        for n in default_obj['nodes']:
            # for each node in the network, add it to the network
            if n['type'] == "state":
                node = edu.stlawu.janeral.states.State(
                    n['attributes']['symbolSet'],
                    enable = edu.stlawu.janeral.mnrl.MNRLDefs.fromMNRLEnable(n['enable']),
                    id = n['id'],
                    report = n['report'],
                    latched = n['attributes']['latched'] if 'latched' in n['attributes'] else False,
                    reportId = n['attributes']['reportId'] if 'reportId' in n['attributes'] else None,
                    attributes = n['attributes']
                )
            elif n['type'] == "hState":
                node = edu.stlawu.janeral.states.HState(
                    n['attributes']['symbolSet'],
                    enable = edu.stlawu.janeral.mnrl.MNRLDefs.fromMNRLEnable(n['enable']),
                    id = n['id'],
                    report = n['report'],
                    latched = n['attributes']['latched'] if 'latched' in n['attributes'] else False,
                    reportId = n['attributes']['reportId'] if 'reportId' in n['attributes'] else None,
                    attributes = n['attributes']
                )
            elif n['type'] == "upCounter":
                node = edu.stlawu.janeral.states.UpCounter(
                    n['attributes']['threshold'],
                    mode = edu.stlawu.janeral.mnrl.MNRLDefs.fromMNRLCounterMode(n['attributes']['mode']),
                    id = n['id'],
                    report = n['report'],
                    reportId = n['attributes']['reportId'] if 'reportId' in n['attributes'] else None,
                    attributes = n['attributes']
                )
            elif n['type'] == "Boolean":
                if n['attributes']['gateType'] not in edu.stlawu.janeral.mnrl.MNRLDefs.BOOLEAN_TYPES:
                    raise mnrlerror.edu.stlawu.janeral.err.InvalidGateType(n['attributes']['gateType'])
                node = Boolean(
                    n['attributes']['gateType'],
                    portCounte=edu.stlawu.janeral.mnrl.MNRLDefs.BOOLEAN_TYPES[n['attributes']['gateType']],
                    id = n['id'],
                    enable = edu.stlawu.janeral.mnrl.MNRLDefs.fromMNRLEnable(n['enable']),
                    report = n['report'],
                    reportId = n['attributes']['reportId'] if 'reportId' in n['attributes'] else None,
                    attributes = n['attributes']
                )
            else:
                # convert input defs into format needed for constructor
                ins = list()
                for k in n['inputDefs']:
                    ins.append((k['portId'],k['width']))

                # convert output defs into format needed for constructor
                outs = list()
                for k in n['outputDefs']:
                    outs.append((k['portId'], k['width']))

                node = edu.stlawu.janeral.mnrl.MNRLNode(
                    id = n['id'],
                    enable = edu.stlawu.janeral.mnrl.MNRLDefs.fromMNRLEnable(n['enable']),
                    report = n['report'],
                    inputDefs = ins,
                    outputDefs = outs,
                    attributes = n['attributes']
                )

            # add the node to the network
            mnrl_obj.addNode(node)

        for n in default_obj['nodes']:
            # for each node, add all the connections
            for k in n['outputDefs']:
                # for each output port
                for c in k['activate']:
                    mnrl_obj.addConnection(
                        (n['id'],k['portId']),
                        (c['id'],c['portId'])
                    )

        return mnrl_obj

 */

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stlawu.janeral.err.*;
import edu.stlawu.janeral.states.MNRLBoolean;
import edu.stlawu.janeral.states.MNRLHState;
import edu.stlawu.janeral.states.MNRLState;
import edu.stlawu.janeral.states.UpCounter;

import java.util.*;

import static edu.stlawu.janeral.mnrl.Janeral.mapper;

public class MNRLDecoder extends ObjectMapper {
    public MNRLNetwork decode(final JsonNode json) {
        final MNRLNetwork mnrlObj;

        //System.out.println(json.toPrettyString());

        // Creating the edu.stlawu.janeral.mnrl.MNRL representation
        mnrlObj = new MNRLNetwork(json.get("id").asText());

        // Creating the edu.stlawu.janeral.mnrl.MNRL network:
        //      1. Add all the nodes
        //      2. Add all the connections

        for (Iterator<JsonNode> it = json.get("nodes").elements(); it.hasNext(); ) {
            final JsonNode n = it.next();
            // For each node in the network, add it to the network
            final MNRLNode node;
            switch (n.get("type").asText()) {
                case "state" -> {
                    final JsonNode attrNode = n.get("attributes");
                    // Gathering the outputSymbols
                    final List<Map.Entry<String, String>> list = new ArrayList<>();
                    Iterator<Map.Entry<String, JsonNode>> fields = n.get("symbolSet").fields();
                    fields.forEachRemaining(field -> {
                        list.add(new AbstractMap.SimpleEntry<>(field.getKey(), field.getKey()));
                    });
                    final MNRLOutputSymbols.ListSymbols outputSymbols =
                            new MNRLOutputSymbols().constructAsListOfPairs(list);

                    // Gathering the attributes
                    final MNRLAttributes attributes = new MNRLAttributes(null, null,
                            attrNode.get("reportId").toString(),
                            outputSymbols,
                            attrNode.get("latched").asBoolean(), 0);
                    try {
                        node = new MNRLState(
                                outputSymbols,
                                MNRLDefs.fromMNRLEnable(n.get("enable").asText()),
                                n.get("id").asText(),
                                n.get("report").asBoolean(),
                                attributes.getReportId(),
                                attributes.isLatched(),
                                attributes
                        );
                    } catch (PortDefError | EnableError e) {
                        throw new RuntimeException(e);
                    }
                }
                case "hState" -> {
                    final JsonNode attrNode = n.get("attributes");

                    // Gathering the attributes
                    final MNRLAttributes attributes = new MNRLAttributes(null, null,
                            attrNode.get("reportId").asText(),
                            attrNode.get("symbolSet").asText(),
                            attrNode.get("latched").asBoolean(), 0);
                    try {
                        node = new MNRLHState(
                                new MNRLOutputSymbols().constructAsString(attributes.getSymbolSet().toString()),
                                MNRLDefs.fromMNRLEnable(n.get("enable").asText()),
                                n.get("id").asText(),
                                n.get("report").asBoolean(),
                                attributes.getReportId(),
                                attributes.isLatched(),
                                attributes
                        );
                    } catch (EnableError | PortDefError e) {
                        throw new RuntimeException(e);
                    }
                }
                case "upCounter" -> {
                    final JsonNode attrNode = n.get("attributes");

                    // Gathering the attributes
                    final MNRLAttributes attributes = new MNRLAttributes(null,
                            attrNode.get("mode").asText(),
                            attrNode.get("reportId").asText(), null, false,
                            attrNode.get("threshold").asInt());

                    try {
                        node = new UpCounter(
                                attributes.getThreshold(),
                                MNRLDefs.fromMNRLCounterMode(attributes.getMode()),
                                n.get("id").asText(),
                                n.get("report").asBoolean(),
                                attributes.getReportId(),
                                attributes
                        );
                    } catch (UpCounterThresholdError | UpCounterModeError | EnableError | PortDefError e) {
                        throw new RuntimeException(e);
                    }
                }
                case "Boolean" -> {
                    final JsonNode attrNode = n.get("attributes");

                    // Gathering the attributes
                    final MNRLAttributes attributes = new MNRLAttributes(
                            attrNode.get("gateType").asText(), null,
                            attrNode.get("reportId").asText(), null, false, 0);

                    final boolean booleanTypeExists = MNRLDefs.BOOLEAN_TYPES.containsKey(attributes.getGateType());
                    try {
                        if (!booleanTypeExists) {
                            throw new InvalidGateType(attributes.getGateType());
                        }
                        node = new MNRLBoolean(
                                attributes.getGateType(),
                                MNRLDefs.BOOLEAN_TYPES.get(attributes.getGateType()),
                                n.get("id").asText(),
                                MNRLDefs.fromMNRLEnable(n.get("enable").asText()),
                                n.get("report").asBoolean(),
                                attributes.getReportId(),
                                attributes
                        );
                    } catch (InvalidGatePortCount | EnableError | PortDefError | InvalidGateFormat | InvalidGateType e) {
                        throw new RuntimeException(e);
                    }
                }
                default -> {
                    // Convert input defs into the format needed for the constructor
                    final List<Map.Entry<String, Object>> ins = new ArrayList<>();
                    for (Iterator<JsonNode> itIns = n.get("inputDefs").elements(); itIns.hasNext(); ) {
                        final JsonNode i = itIns.next();
                        ins.add(new AbstractMap.SimpleEntry<>(i.get("portId").asText(), i.get("width").asText()));
                    }

                    // Convert output defs into the format needed for the constructor
                    final List<Map.Entry<String, Object>> outs = new ArrayList<>();
                    for (Iterator<JsonNode> itOuts = n.get("outputDefs").elements(); itOuts.hasNext(); ) {
                        final JsonNode o = itOuts.next();
                        outs.add(new AbstractMap.SimpleEntry<>(o.get("portId").asText(), o.get("width").asText()));
                    }

                    // Gathering the attributes
                    final MNRLAttributes attributes =
                            new MNRLAttributes(mapper.convertValue(n.get("attributes"), new TypeReference<HashMap<String, Object>>() {}));

                    try {
                        node = new MNRLNode(
                                n.get("id").asText(),
                                MNRLDefs.fromMNRLEnable(n.get("enable").asText()),
                                n.get("report").asBoolean(),
                                ins,
                                outs,
                                attributes
                        );
                    } catch (EnableError | PortDefError e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            // Add the node to the network
            mnrlObj.addNode(node);
        }

        for (Iterator<JsonNode> it = json.get("nodes").elements(); it.hasNext(); ) {
            final JsonNode n = it.next();
            for (Iterator<JsonNode> it2 = n.get("outputDefs").elements(); it2.hasNext(); ) {
                final JsonNode k = it2.next();
                for (Iterator<JsonNode> it3 = k.get("activate").elements(); it3.hasNext(); ) {
                    final JsonNode c = it3.next();
                    try {
                        mnrlObj.addConnection(
                                new AbstractMap.SimpleEntry<>(n.get("id").asText(), k.get("portId").asText()),
                                new AbstractMap.SimpleEntry<>(c.get("id").asText(), c.get("portId").asText())
                        );
                    } catch (UnknownPort | UnknownNode | PortWidthMismatch e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }


        return mnrlObj;
    }
}
