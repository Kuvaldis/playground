package kuvaldis.play.redis;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class RedisTest {

    @Test
    public void testHelloWorld() throws Exception {
        final Jedis jedis = new Jedis("localhost");
        // key/value
        jedis.del("foo");
        jedis.set("foo", "bar");
        assertEquals("bar", jedis.get("foo"));
        jedis.del("foo");
        assertNull(jedis.get("foo"));
        // list
        jedis.del("list");
        jedis.rpush("list", "val1", "val2");
        jedis.lpush("list", "val0");
        final List<String> list = jedis.lrange("list", 0, -1);
        assertEquals(Arrays.asList("val0", "val1", "val2"), list);
        jedis.rpop("list");
        final List<String> list1 = jedis.lrange("list", 0, -1);
        assertEquals(Arrays.asList("val0", "val1"), list1);
        jedis.rpush("list", "val2", "val3", "val4");
        final List<String> list2 = jedis.lrange("list", 0, -1);
        assertEquals(Arrays.asList("val0", "val1", "val2", "val3", "val4"), list2);
        jedis.ltrim("list", 2, 3);
        final List<String> list3 = jedis.lrange("list", 0, -1);
        assertEquals(Arrays.asList("val2", "val3"), list3);
        // hash map
        jedis.del("user:1000");
        final Map<String, String> user = new LinkedHashMap<>();
        user.put("username", "jaap");
        user.put("birthyear", "2015");
        jedis.hmset("user:1000", user);
        final String username = jedis.hget("user:1000", "username");
        assertEquals("jaap", username);
    }

    @Test
    public void testPool() throws Exception {
        final JedisPool pool = new JedisPool(new JedisPoolConfig(), "localhost");
        try (final Jedis jedis = pool.getResource()) {
//            jedis.set()
        }
    }
}
