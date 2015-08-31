package kuvaldis.play.elasticsearch;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
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
        final SearchResponse response = client.prepareSearch("bank")
                .setTypes("account")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setSize(0)
                .setQuery(QueryBuilders.matchAllQuery())
                .addAggregation(AggregationBuilders
                        .range("group_by_age")
                        .field("age")
                        .addRange("2030", 20, 30)
                        .addRange("3040", 30, 40)
                        .addRange("4050", 40, 50)
                        .subAggregation(AggregationBuilders
                                .terms("group_by_gender")
                                .field("gender").subAggregation(AggregationBuilders
                                        .avg("average_balance")
                                        .field("balance"))))
                .execute()
                .actionGet();
        final Range groupByAge = response.getAggregations().get("group_by_age");
        assertEquals(3, groupByAge.getBuckets().size());

        final Range.Bucket ageBucket1 = groupByAge.getBucketByKey("2030");
        final Range.Bucket ageBucket2 = groupByAge.getBucketByKey("3040");
        final Range.Bucket ageBucket3 = groupByAge.getBucketByKey("4050");
        assertEquals(451, ageBucket1.getDocCount());
        assertEquals(504, ageBucket2.getDocCount());
        assertEquals(45, ageBucket3.getDocCount());

        final Terms groupByGender = ageBucket1.getAggregations().get("group_by_gender");
        final Terms.Bucket genderBucketMale = groupByGender.getBucketByKey("m");
        final Terms.Bucket genderBucketFemale = groupByGender.getBucketByKey("f");
        assertEquals(232, genderBucketMale.getDocCount());
        assertEquals(219, genderBucketFemale.getDocCount());

        final Avg averageBalance = genderBucketMale.getAggregations().get("average_balance");
        assertEquals(27374.05172413793, averageBalance.getValue(), 0.01);
    }
}
