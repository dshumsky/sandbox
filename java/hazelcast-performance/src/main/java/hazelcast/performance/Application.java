package hazelcast.performance;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.hazelcast.core.IMap;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.PredicateBuilder;
import lombok.extern.slf4j.Slf4j;

import static java.lang.System.currentTimeMillis;

@SpringBootApplication
@EnableTransactionManagement
@Slf4j
public class Application {

    interface Opts {
        String QUERY = "query";
        String FILE = "file";
        String N = "n";
        String FETCH_SIZE = "fetchSize";
    }

    @Autowired
    DatabaseFacade databaseFacade;

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(final ApplicationContext ctx) {
        return new CommandLineRunner() {
            @Override
            public void run(final String... args) throws Exception {

                // final Options options = new Options();
                //
                // Option o = new Option("q", Opts.QUERY, true, "query to execute");
                // o.setRequired(false);
                // options.addOption(o);
                //
                // o = new Option("f", Opts.FILE, true, "file with query to execute");
                // o.setRequired(false);
                // options.addOption(o);
                //
                // o = new Option("n", Opts.N, true, "output every N-th row (default: 1000)");
                // o.setRequired(false);
                // o.setType(Number.class);
                // options.addOption(o);
                //
                // o = new Option("s", Opts.FETCH_SIZE, true, "fetchSize (default: 10)");
                // o.setRequired(false);
                // o.setType(Number.class);
                // options.addOption(o);
                //
                // final CommandLineParser parser = new DefaultParser();
                // final HelpFormatter formatter = new HelpFormatter();
                // final CommandLine cmd;
                //
                // cmd = parser.parse(options, args);
                // String query = cmd.getOptionValue(Opts.QUERY);
                // final Number n = (Number) cmd.getParsedOptionValue(Opts.N);
                // final Number fetchSize = (Number) cmd.getParsedOptionValue(Opts.FETCH_SIZE);

                ExecutorService executor = Executors.newFixedThreadPool(2);
                HazelcastFacade hazelcastFacade = new HazelcastFacade();

                IMap<ItemSaleKey, ItemSaleValue> salesMap = hazelcastFacade.getSalesMap();
                IMap<TodoKey, TodoValue> todoMap = hazelcastFacade.getTodoMap();

                Map<Long, Long> whCount = new HashMap<>();

                Future<?> itemSalesFuture = executor.submit(
                        () -> databaseFacade.loadItemSales(pair -> {
                            salesMap.put(pair.getValue0(), pair.getValue1());
                            long whId = pair.getValue0().getWhId();
                            Long count = whCount.get(whId);
                            whCount.put(whId, count != null ? count + 1 : 1);
                        }, 100_000L));
                Future<?> todoFuture = executor.submit(
                        () -> databaseFacade.loadTodos(pair -> {
                            todoMap.put(pair.getValue0(), pair.getValue1());
                            long whId = pair.getValue0().getWhId();
                            Long count = whCount.get(whId);
                            whCount.put(whId, count != null ? count + 1 : 1);
                        }, 2_000_000L));

                itemSalesFuture.get();
                todoFuture.get();

                log.info("salesMap.size={}", salesMap.size());
                log.info("todoMap.size={}", todoMap.size());
                log.info("whCount.size={}", whCount.size());

                long start = currentTimeMillis();
                long previous = currentTimeMillis();
                long count = 0;

                for (Long whId : whCount.keySet()) {
                    Predicate predicate = new PredicateBuilder()
                            .getEntryObject()
                            .key().get(TodoKey.Fields.whId).equal(whId);
                    Set<Entry<TodoKey, TodoValue>> entries = todoMap.entrySet(predicate);
                    long current = currentTimeMillis();
                    count++;
                    log.info("wh={}, entries.size={}, time={}, averageTime={}",
                        whId, entries.size(), (current - previous), (current - start) / count);
                    previous = current;
                }
            }
        };
    }

//    class X extends AbstractEntryProcessor<ItemSaleKey, List<ItemSaleValue>> {
//
//        private final ItemSaleValue value;
//
//        public X(ItemSaleValue value) {
//            this.value = value;
//        }
//
//        @Override
//        public Object process(Entry<ItemSaleKey, List<ItemSaleValue>> entry) {
//            List<ItemSaleValue> list = entry.getValue();
//            if (list == null) {
//                list = new ArrayList<>(4);
//            }
//            list.add(value);
//            entry.setValue(list);
//            return entry;
//        }
//    }
}
