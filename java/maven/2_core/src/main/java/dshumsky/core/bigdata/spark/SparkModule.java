package dshumsky.core.bigdata.spark;

import com.google.inject.AbstractModule;
import dshumsky.core.bigdata.spark.temperature.TemperatureModule;

/**
 * @author Dmitry Shumski (dshumsky@gmail.com)
 */
public abstract class SparkModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new TemperatureModule());
        bindSparkContext();
    }

    public abstract void bindSparkContext();
}
