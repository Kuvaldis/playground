package kuvaldis.play.elasticsearch;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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
        DeleteResponse deleteResponse = client.prepareDelete("twitter", "tweet", "1")
                .execute()
                .actionGet();
        deleteResponse.isFound();

        final IndexResponse response = client.prepareIndex("twitter", "tweet", "1")
                .setSource(jsonBuilder()
                        .startObject()
                        .field("user", "kimchy")
                        .field("postDate", new Date())
                        .field("message", "trying out Elasticsearch")
                        .endObject())
                .execute()
                .actionGet();
        assertEquals("twitter", response.getIndex());
        assertEquals("tweet", response.getType());
        assertEquals("1", response.getId());

        client.prepareUpdate("twitter", "tweet", "1")
                .setDoc(jsonBuilder()
                        .startObject()
                        .field("user", "mrbeen")
                        .endObject())
                .execute()
                .actionGet();
        final GetResponse afterUpdateGetResponse = client.prepareGet("twitter", "tweet", "1")
                .execute()
                .actionGet();
        assertEquals("mrbeen", afterUpdateGetResponse.getSource().get("user"));

        client.prepareDelete("twitter", "tweet", "1")
                .execute()
                .actionGet();
        final GetResponse getResponse = client.prepareGet("twitter", "tweet", "1")
                .execute()
                .actionGet();
        assertFalse(getResponse.isExists());
    }

    @Test
    public void testGroupByAgeRangesAndInsideTheGroupsGroupByGenderWithAverageBalance() throws Exception {
//        final SearchResponse response = client.prepareSearch("bank")
//                .setTypes("account")
//                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
//                .setQuery()

    }
}
