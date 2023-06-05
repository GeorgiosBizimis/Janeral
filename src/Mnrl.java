import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;

import java.io.*;
import java.util.Arrays;
import java.util.Set;

public class Mnrl {

    /*
    def loadMNRL(filename):
    with open(os.path.dirname(os.path.abspath(__file__))+"/mnrl-schema.json", "r") as s:
        schema = json.load(s)
    with open(filename, "r") as f:
        json_string = f.read()

        try:
            jsonschema.validate(json.loads(json_string),schema)
        except jsonschema.exceptions.ValidationError as e:
            print "ERROR:", e
            return None

        # parse into MNRL
        d = MNRLDecoder()
        return d.decode(json_string)
     */

    public static void main(String[] args) {
        loadMNRL("24_20x3.1chip.mnrl");
    }

    public static Object loadMNRL(final String filename) {
        final File f = new File("src/" + filename);
        JsonNode jsonNode = null;
        if (f.exists()) {
            final Set<ValidationMessage> errors;
            final ObjectMapper mapper = new ObjectMapper();
            final JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
            final JsonSchema jsonSchema = factory.getSchema(Mnrl.class.getResourceAsStream("/mnrl-schema.json"));
            try {
                jsonNode = mapper.readTree(Mnrl.class.getResourceAsStream("/" + filename));
                errors = jsonSchema.validate(jsonNode);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.err.println(Arrays.toString(errors.toArray()));
        } else {
            System.err.println("Error: File " + filename + " could not be found!");
        }
        //System.out.println(jsonNode.toPrettyString());
        return null;//TODO MNRLObject
    }

}
