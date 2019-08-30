package dshumsky.core.bigdata.spark.temperature;

import java.io.Serializable;

import org.apache.spark.api.java.function.Function2;

import com.google.common.base.Preconditions;

/**
 * @author Dmitry Shumski (dshumsky@gmail.com)
 */
public class TemperatureByTimeAggregation implements Serializable {

    public static Function2<TemperatureByTimeAggregation, TemperatureByTimeAggregation, TemperatureByTimeAggregation>
            REDUCE = (Function2<TemperatureByTimeAggregation, TemperatureByTimeAggregation, TemperatureByTimeAggregation>)
            (v1, v2) -> {
                Preconditions.checkState(v1.time == v2.time, "Time should be the same");
                return new TemperatureByTimeAggregation(v1.time,
                        (v1.count * v1.averageTemperature + v2.count * v2.averageTemperature) / (v1.count + v2.count),
                        v1.count + v2.count);
            };
    private final long time;
    private final double averageTemperature;
    private final int count;

    public TemperatureByTimeAggregation(TemperatureRecord record) {
        this.time = record.getTime();
        this.averageTemperature = record.getTemperature();
        this.count = 1;
    }

    public TemperatureByTimeAggregation(long time, double averageTemperature, int count) {
        this.time = time;
        this.averageTemperature = averageTemperature;
        this.count = count;
    }

    public long getTime() {
        return time;
    }

    public double getAverageTemperature() {
        return averageTemperature;
    }

    public int getCount() {
        return count;
    }
}
