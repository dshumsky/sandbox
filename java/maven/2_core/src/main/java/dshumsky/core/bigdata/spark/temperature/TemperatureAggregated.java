package dshumsky.core.bigdata.spark.temperature;

import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.assistedinject.Assisted;
import dshumsky.core.bigdata.hadoop.HadoopFile;
import scala.Tuple2;

public class TemperatureAggregated {

    private final Provider<JavaSparkContext> sc;

    private final HadoopFile file;

    @Inject
    public TemperatureAggregated(Provider<JavaSparkContext> sc, @Assisted HadoopFile file) {
        this.sc = sc;
        this.file = file;
    }

    public Map<Long, TemperatureByTimeAggregation> aggregationByTime() throws IOException {
        JavaPairRDD<LongWritable, Text> rdd = sc.get().hadoopFile(file.getPath(), TextInputFormat.class, LongWritable.class, Text.class);
        JavaRDD<TemperatureRecord> records = rdd.map((Function<Tuple2<LongWritable, Text>, TemperatureRecord>) v -> TemperatureRecord.parseFromTextLine(v._2().toString()));
        JavaRDD<TemperatureRecord> notNullRecords = records.filter((Function<TemperatureRecord, Boolean>) r -> r != null);
        JavaPairRDD<Long, TemperatureByTimeAggregation> timeAggregationPairRDD = notNullRecords.mapToPair((PairFunction<TemperatureRecord, Long, TemperatureByTimeAggregation>) r -> new Tuple2<>(r.getTime(), new TemperatureByTimeAggregation(r)));
        JavaPairRDD<Long, TemperatureByTimeAggregation> aggregationByTime = timeAggregationPairRDD.reduceByKey(TemperatureByTimeAggregation.REDUCE);

        return aggregationByTime.collectAsMap();
    }
}