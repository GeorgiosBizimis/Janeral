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

    public static MNRLNetwork loadMNRL(final String filename) throws IOException {
        final File file = new File(filename);
        JsonNode jsonNode;
        if (file.exists()) {
            final Set<ValidationMessage> errors;
            final JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
            final JsonSchema jsonSchema = factory.getSchema(Janeral.class.getResourceAsStream("/edu/stlawu/janeral/resources/mnrl-schema.json"));
            try {
                jsonNode = mapper.readTree(new FileInputStream(file));
                errors = jsonSchema.validate(jsonNode);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if(!errors.isEmpty()) System.err.println("ERROR: " + Arrays.toString(errors.toArray()));
        } else {
            throw new IOException("ERROR: File " + filename + " could not be found!");
        }

        final MNRLDecoder d = new MNRLDecoder();
        return d.decode(jsonNode);
    }

}
