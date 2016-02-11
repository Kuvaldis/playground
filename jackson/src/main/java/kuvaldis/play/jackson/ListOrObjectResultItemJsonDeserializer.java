package kuvaldis.play.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListOrObjectResultItemJsonDeserializer extends JsonDeserializer<List<ResultItem>> {

    @Override
    public List<ResultItem> deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException, JsonProcessingException {
        final ObjectCodec objectCodec = p.getCodec();
        final JsonNode listOrObjectNode = objectCodec.readTree(p);
        final List<ResultItem> result = new ArrayList<>();
        if (listOrObjectNode.isArray()) {
            for (JsonNode node : listOrObjectNode) {
                result.add(objectCodec.treeToValue(node, ResultItem.class));
            }
        } else {
            result.add(objectCodec.treeToValue(listOrObjectNode, ResultItem.class));
        }
        return result;
    }
}
