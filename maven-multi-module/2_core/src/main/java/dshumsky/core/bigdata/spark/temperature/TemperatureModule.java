package dshumsky.core.bigdata.spark.temperature;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * @author Dmitry Shumski (dshumsky@gmail.com)
 */
public class TemperatureModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(TemperatureAggregated.class, TemperatureAggregated.class)
                .build(TemperatureAggregatedFactory.class));
    }
}
