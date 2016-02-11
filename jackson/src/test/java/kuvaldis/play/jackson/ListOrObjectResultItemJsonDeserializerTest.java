package kuvaldis.play.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class ListOrObjectResultItemJsonDeserializerTest {

    @Test
    public void testDeserialize() throws Exception {
        final String json1 = "{\"result\": [{\"value\": \"test\"}]}";
        final Result result1 = new ObjectMapper().readValue(json1, Result.class);
        final String json2 = "{\"result\": {\"value\": \"test\"}}";
        final Result result2 = new ObjectMapper().readValue(json2, Result.class);
//        final Result result = new Result();
//        final ResultItem item1 = new ResultItem();
//        final ResultItem item2 = new ResultItem();
//        result.setResult(Arrays.asList(item1, item2));
//        System.out.println(new ObjectMapper().writeValueAsString(result));
    }
}