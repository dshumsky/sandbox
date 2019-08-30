package dshumsky.core.bigdata.spark.temperature;

import java.io.Serializable;
import java.util.Iterator;

import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Splitter;

/**
 * @author Dmitry Shumski (dshumsky@gmail.com)
 */
public class TemperatureRecord implements Serializable {
    private final long time;
    private final double temperature;

    public TemperatureRecord(long time, double temperature) {
        this.time = time;
        this.temperature = temperature;
    }

    public long getTime() {
        return time;
    }

    public double getTemperature() {
        return temperature;
    }

    /**
     * @return null, if parsing was not successful
     */
    @Nullable
    public static TemperatureRecord parseFromTextLine(String line){
        try {
            Iterator<String> parts = Splitter.on(",").limit(2).split(line).iterator();
            long time = Long.parseLong(parts.next());
            double temperature = Double.parseDouble(parts.next());
            return new TemperatureRecord(time, temperature);
        } catch (Exception e) {
            return null;
        }
    }

    public String textLine() {
        return new StringBuilder()
                .append(time).append(",")
                .append(temperature)
                .toString();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("time", time)
                .add("temperature", temperature)
                .toString();
    }
}
