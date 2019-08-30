package dshumsky.core.bigdata.integration.spark;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.UsingSteps;
import org.jbehave.core.annotations.When;
import org.jbehave.core.annotations.guice.UsingGuice;
import org.jbehave.core.junit.guice.GuiceAnnotatedEmbedderRunner;
import org.jbehave.core.model.ExamplesTable;
import org.junit.runner.RunWith;

import com.google.inject.Inject;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import dshumsky.core.bigdata.hadoop.HadoopFile;
import dshumsky.core.bigdata.hadoop.HadoopFileFactory;
import dshumsky.core.bigdata.integration.hadoop.HadoopTestModule;
import dshumsky.core.bigdata.integration.hadoop.step.HDFSOperations;
import dshumsky.core.bigdata.spark.WordCountStreamContext;
import dshumsky.core.tests.jbehave.JBehaveStory;
import dshumsky.core.tests.jbehave.JBehaveTableAssert;
import dshumsky.core.tests.jbehave.JBehaveTableAssert.PrintCellIfErrorStrategy;
import scala.Tuple2;

/**
 * @author dshumski (Dmitry.Shumski@mgm-tp.com)
 */
@RunWith(GuiceAnnotatedEmbedderRunner.class)
@UsingGuice(modules = { HadoopTestModule.class })
@UsingSteps(instances = { HDFSOperations.class, WordCountStreamContextTest.class })
public class WordCountStreamContextTest extends JBehaveStory {

    @Inject
    private HadoopFileFactory fileFactory;

    private final HazelcastInstance hazelcastInstance;

    public static final VoidFunction<JavaRDD<Tuple2<String, Integer>>> foreachWordCountStateFunction =
            rdd -> {
                List<Tuple2<String, Integer>> list = rdd.collect();
                if (!list.isEmpty()) {
                    Map<String, Integer> map = list.stream().collect(Collectors.toMap(Tuple2::_1, Tuple2::_2));
                    System.out.println("map=" + Arrays.toString(map.entrySet().toArray()));
                    ClientConfig clientConfig = new ClientConfig();
                    clientConfig.getNetworkConfig().addAddress("127.0.0.1:5701");
                    HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
                    final IMap<String, Integer> hazelcastMap = client.getMap("wordCounts");
                    hazelcastMap.putAll(map);
                }
            };
    private WordCountStreamContext streamContext;


    public WordCountStreamContextTest() {
        Config cfg = new Config();
        hazelcastInstance = Hazelcast.newHazelcastInstance(cfg);
    }

    @When("start counting")
    public void startCounting() throws IOException, InterruptedException {
        HadoopFile checkpointDirectory = fileFactory.create("/words/checkpointDirectory");
        HadoopFile toProcessDirectory = fileFactory.create("/words/toProcess");
        final String toProcessDirectoryPath = toProcessDirectory.getPath();
        // jsc -> jsc.socketTextStream("localhost", 9999);
        Function<JavaStreamingContext, JavaDStream<String>> inputStream = jsc -> jsc.textFileStream(toProcessDirectoryPath);
        streamContext = new WordCountStreamContext(checkpointDirectory.getPath(),
                inputStream,
                foreachWordCountStateFunction);
        streamContext.getContext().sparkContext().setLogLevel("ERROR");
        streamContext.getContext().start();
    }

    @When("sleep $ms ms")
    public void sleep(long ms) throws IOException, InterruptedException {
        Thread.sleep(ms);
    }

    @Then("checkStatus $table")
    public void checkStatus(ExamplesTable table) {
        Map<Integer, String> wordCounts = hazelcastInstance.getMap("wordCounts");
        JBehaveTableAssert tableAssert = new JBehaveTableAssert(table);
        tableAssert.assertEqualsTo(wordCounts, "-", PrintCellIfErrorStrategy.EXPECTED_AND_ACTUAL);
    }

    @Then("stop")
    public void stop() {
        streamContext.getContext().stop();
        streamContext.getContext().awaitTermination();
    }
}
