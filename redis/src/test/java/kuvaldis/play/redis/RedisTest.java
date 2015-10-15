package kuvaldis.play.redis;

import org.junit.Test;
import redis.clients.jedis.Jedis;

import static org.junit.Assert.assertEquals;

public class RedisTest {

    @Test
    public void testHelloWorld() throws Exception {
        final Jedis jedis = new Jedis("localhost");
        jedis.set("foo", "bar");
        assertEquals("bar", jedis.get("foo"));
    }
}
