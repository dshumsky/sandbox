package dshumsky.core.bigdata.spark;

import java.io.Serializable;
import java.util.Arrays;
import java.util.function.Function;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function3;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.State;
import org.apache.spark.streaming.StateSpec;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaMapWithStateDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.api.java.JavaStreamingContextFactory;

import com.google.common.base.Optional;
import scala.Tuple2;

/**
 * @author dshumski (Dmitry.Shumski@mgm-tp.com)
 */
@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "Guava"})
public class WordCountStreamContext implements Serializable {

    private final JavaStreamingContext context;

    public WordCountStreamContext(String checkpointDirectory,
                                  Function<JavaStreamingContext, JavaDStream<String>> inputStreamBuilder,
                                  VoidFunction<JavaRDD<Tuple2<String, Integer>>> foreachWordCountStateFunction) {
        SparkConf conf = new SparkConf().setMaster("local[2]").setAppName("NetworkWordCount");
        JavaStreamingContextFactory contextFactory = () -> {
            // Create a local StreamingContext with two working thread and batch interval of 1 second
            JavaStreamingContext ssc = new JavaStreamingContext(conf, Durations.seconds(1));
            ssc.checkpoint(checkpointDirectory);

            JavaDStream<String> lines = inputStreamBuilder.apply(ssc);

            // Split each line into words
            JavaDStream<String> words = lines.flatMap(s -> Arrays.asList(s.split(" ")));

            // Count each word in each batch
            JavaPairDStream<String, Integer> pairs = words.mapToPair(s -> new Tuple2<>(s, 1));
            JavaPairDStream<String, Integer> wordCounts = pairs.reduceByKey((i1, i2) -> i1 + i2);

            Function3<String, Optional<Integer>, State<Integer>, Tuple2<String, Integer>> stateFunction = (word, one, state) -> {
                int sum = one.or(0) + (state.exists() ? state.get() : 0);
                Tuple2<String, Integer> output = new Tuple2<>(word, sum);
                state.update(sum);
                return output;
            };

            JavaMapWithStateDStream<String, Integer, Integer, Tuple2<String, Integer>> wordCountsState =
                    wordCounts.mapWithState(StateSpec.function(stateFunction));


            wordCountsState.foreachRDD(foreachWordCountStateFunction);
            return ssc;
        };

        // Get JavaStreamingContext from checkpoint data or create a new one
        context = JavaStreamingContext.getOrCreate(checkpointDirectory, contextFactory);
    }

    public JavaStreamingContext getContext() {
        return context;
    }
}
