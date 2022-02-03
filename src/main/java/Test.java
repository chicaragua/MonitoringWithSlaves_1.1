import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.File;
import java.io.IOException;

public class Test {
    public static void main(String[] args) {
        int argsLength = args.length;

        final String eid = args[0];
        final String field = args[1];
        final String pathToJson = args[2];
        if (argsLength == 4) {
            final String uuid = args[3];
        }

        System.out.println(argsLength);

        final ObjectMapper mapper = new ObjectMapper();

        try {
            final JsonNode jsonNode = mapper.readTree(new File(pathToJson));

            if (jsonNode.isArray()) {
                final ArrayNode arrayOfServerDescription = (ArrayNode) jsonNode;

                for (JsonNode node : arrayOfServerDescription) {
                    if (node.get("eid").asText().equals(eid)) {
                        if (argsLength == 3) {
                            System.out.println(node.get(field));
                            return;
                        }

                        final JsonNode nodes = node.get("nodes");

                        if (nodes.isArray()) {
                            final ArrayNode arrayOfNodes = (ArrayNode) nodes;

                            for (JsonNode serverNode: arrayOfNodes) {
                                if (serverNode.get("uuid").asText().equals(args[3])) {
                                    System.out.println(serverNode.get(field));
                                    return;
                                }
                            }
                        } else {
                            throw new RuntimeException("nodes is not array");
                        }
                        return;
                    }
                }

                throw new RuntimeException("Server with eid = " + eid + " not found");
            } else {
                throw new RuntimeException("Not valid json. must be array of objects");
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to read json file: " + pathToJson);
        }
    }
}
