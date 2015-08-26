package kuvaldis.play.elasticsearch;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.junit.Assert.assertEquals;

public class ElasticSearchHelloWorldTest {

    private Client client;

    @Before
    public void setUp() throws Exception {
        client = new TransportClient(ImmutableSettings.settingsBuilder()
                .put("cluster.name", "elasticsearch").build())
                .addTransportAddress(new InetSocketTransportAddress("localhost", 9300));
    }

    @Test
    public void testIndexDocument() throws Exception {
        final IndexResponse response = client.prepareIndex("twitter", "tweet", "1")
                .setSource(jsonBuilder()
                                .startObject()
                                .field("user", "kimchy")
                                .field("postDate", new Date())
                                .field("message", "trying out Elasticsearch")
                                .endObject()
                )
                .execute()
                .actionGet();
        assertEquals("twitter", response.getIndex());
        assertEquals("tweet", response.getType());
        assertEquals("1", response.getId());
    }
}
