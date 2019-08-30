package dshumsky.core.bigdata.spark.temperature;

import dshumsky.core.bigdata.hadoop.HadoopFile;

/**
 * @author Dmitry Shumski (dshumsky@gmail.com)
 */
public interface TemperatureAggregatedFactory {
    TemperatureAggregated create(HadoopFile file);
}
