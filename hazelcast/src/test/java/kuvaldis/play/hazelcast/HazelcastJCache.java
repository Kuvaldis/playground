package kuvaldis.play.hazelcast;

import com.hazelcast.cache.ICache;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.junit.Test;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.AccessedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.spi.CachingProvider;

import static org.junit.Assert.assertEquals;

public class HazelcastJCache {
    @Test
    public void testPutToCache() throws Exception {
        final HazelcastInstance instance = Hazelcast.newHazelcastInstance();
        final CachingProvider cachingProvider = Caching.getCachingProvider();
        final CacheManager cacheManager = cachingProvider.getCacheManager();
        final MutableConfiguration<String, String> configuration = new MutableConfiguration<>();
        configuration.setStoreByValue(true)
                .setTypes(String.class, String.class)
                .setExpiryPolicyFactory(AccessedExpiryPolicy.factoryOf(Duration.ONE_MINUTE))
                .setStatisticsEnabled(false);
        cacheManager.createCache("cache", configuration);
        final Cache<String, String> cache = cacheManager.getCache("cache", String.class, String.class);
        cache.put("key", "value");
        final String value = cache.get("key");
        assertEquals("value", value);
        //noinspection unchecked
        final ICache<String, String> iCache = cache.unwrap(ICache.class);
        assertEquals("value", iCache.get("key"));
    }
}
