package hazelcast.performance;

import com.hazelcast.config.InMemoryFormat;
import com.hazelcast.config.MapConfig;

public class SalesMapConfig extends MapConfig {
    public SalesMapConfig(String name) {
        super(name);
        // EvictionConfig evictionConfig = new EvictionConfig()
        // .setEvictionPolicy(EvictionPolicy.NONE)
        // .setMaximumSizePolicy(MaxSizePolicy.ENTRY_COUNT)
        // .setSize(5000);
        // NearCacheConfig nearCacheConfig = new NearCacheConfig()
        // .setInMemoryFormat(InMemoryFormat.OBJECT)
        // .setInvalidateOnChange(false)
        // .setTimeToLiveSeconds(1 * 60 * 60) // 1 hour
        // .setCacheLocalEntries(true)
        // .setEvictionConfig(evictionConfig);
        // .setNearCacheConfig(nearCacheConfig)
        setInMemoryFormat(InMemoryFormat.BINARY);
        setBackupCount(0);
        setAsyncBackupCount(0);
        // setReadBackupData(true);
        // setName(name + ".country_*");
    }
}
