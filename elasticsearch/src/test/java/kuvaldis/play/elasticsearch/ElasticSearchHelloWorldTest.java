package kuvaldis.play.elasticsearch;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.node.Node;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

public class ElasticSearchHelloWorldTest {

    private Client client;
    private Node node;

    @Before
    public void setUp() throws Exception {
        final Node node = nodeBuilder()
                .settings(ImmutableSettings.settingsBuilder().put("http.enabled", false)).client(true)
                .node();
        final Client client = node.client();
        this.node = node;
        this.client = client;
    }

    @After
    public void tearDown() throws Exception {
        node.close();
    }

    @Test
    public void testClient() throws Exception {

    }
}
