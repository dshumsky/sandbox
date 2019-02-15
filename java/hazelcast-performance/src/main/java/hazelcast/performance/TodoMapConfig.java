package hazelcast.performance;

import com.hazelcast.config.InMemoryFormat;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MapIndexConfig;
import com.hazelcast.query.QueryConstants;

public class TodoMapConfig extends MapConfig {
    public TodoMapConfig(String name) {
        super(name);
//         EvictionConfig evictionConfig = new EvictionConfig()
//         .setEvictionPolicy(EvictionPolicy.NONE)
//         .setMaximumSizePolicy(MaxSizePolicy.ENTRY_COUNT)
//         .setSize(5000);
//         NearCacheConfig nearCacheConfig = new NearCacheConfig()
//         .setInMemoryFormat(InMemoryFormat.OBJECT)
//         .setInvalidateOnChange(false)
//         .setTimeToLiveSeconds(1 * 60 * 60) // 1 hour
//         .setCacheLocalEntries(true)
//         .setEvictionConfig(evictionConfig);
//
//        setNearCacheConfig(nearCacheConfig);
        setInMemoryFormat(InMemoryFormat.BINARY);
        setBackupCount(0);
        setAsyncBackupCount(0);
        addMapIndexConfig(new MapIndexConfig(QueryConstants.KEY_ATTRIBUTE_NAME.value() + "#" + TodoKey.Fields.whId, false));
        // setReadBackupData(true);
        // setName(name + ".country_*");
    }
}
