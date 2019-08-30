package hazelcast.performance;

import java.util.Collections;
import java.util.function.Supplier;

import com.hazelcast.config.Config;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.MulticastConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.config.SerializationConfig;
import com.hazelcast.config.TcpIpConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

public class HazelcastFacade implements Supplier<HazelcastInstance> {

    public static final String MAP_SALES = "map-sales";
    public static final String MAP_TODO = "map-todo";
    private final HazelcastInstance hazelcastInstance;
    private final IMap<ItemSaleKey, ItemSaleValue> salesMap;
    private final IMap<TodoKey, TodoValue> todoMap;

    public HazelcastFacade() {
        Config config = new Config();

        config.setGroupConfig(new GroupConfig().setName("HazelcastTest"));
        NetworkConfig networkConfig = new NetworkConfig()
            .setPort(5901)
            .setReuseAddress(true)
            .setJoin(new JoinConfig()
                .setMulticastConfig(
                    new MulticastConfig()
                        .setEnabled(true))
                .setTcpIpConfig(
                    new TcpIpConfig()
                        .setEnabled(false)
                        .setConnectionTimeoutSeconds(5)
                        .setMembers(Collections.singletonList("127.0.0.1"))));
        networkConfig.setPortCount(100);
        config.setNetworkConfig(networkConfig);
        config.getExecutorConfig("hz:query").setPoolSize(4);


        config.getMapConfigs().put(MAP_SALES, new SalesMapConfig(MAP_SALES));
        config.getMapConfigs().put(MAP_TODO, new TodoMapConfig(MAP_TODO));
        config.setSerializationConfig(
            new SerializationConfig().addDataSerializableFactory(MyDataSerializableFactory.ID, new MyDataSerializableFactory()));

        hazelcastInstance = Hazelcast.newHazelcastInstance(config);
        salesMap = hazelcastInstance.getMap(MAP_SALES);
        todoMap = hazelcastInstance.getMap(MAP_TODO);
    }


    @Override
    public HazelcastInstance get() {
        return hazelcastInstance;
    }

    public IMap<ItemSaleKey, ItemSaleValue> getSalesMap() {
        return salesMap;
    }

    public IMap<TodoKey, TodoValue> getTodoMap() {
        return todoMap;
    }
}
