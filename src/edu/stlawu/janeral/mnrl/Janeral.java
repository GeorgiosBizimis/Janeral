package edu.stlawu.janeral.mnrl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;

import java.io.*;
import java.util.Arrays;
import java.util.Set;

public class Janeral {

    public final static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static MNRLNetwork loadMNRL(final String filename) {
        final File f = new File("src/" + filename);
        JsonNode jsonNode = null;
        if (f.exists()) {
            final Set<ValidationMessage> errors;
            final JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
            final JsonSchema jsonSchema = factory.getSchema(Janeral.class.getResourceAsStream("/edu/stlawu/janeral/resources/mnrl-schema.json"));
            try {
                jsonNode = mapper.readTree(Janeral.class.getResourceAsStream("/" + filename));
                errors = jsonSchema.validate(jsonNode);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.err.println("ERROR: " + Arrays.toString(errors.toArray()));
        } else {
            System.err.println("ERROR: File " + filename + " could not be found!");
        }
        //System.out.println(jsonNode.toPrettyString());

        final MNRLDecoder d = new MNRLDecoder();
        return d.decode(jsonNode);
    }

}
