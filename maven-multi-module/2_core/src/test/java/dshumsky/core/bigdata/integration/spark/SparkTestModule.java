package dshumsky.core.bigdata.integration.spark;

import org.apache.spark.api.java.JavaSparkContext;

import dshumsky.core.bigdata.spark.SparkModule;

/**
 * @author Dmitry Shumski (dshumsky@gmail.com)
 */
public class SparkTestModule extends SparkModule {
    @Override
    public void bindSparkContext() {
        bind(JavaSparkContext.class).toProvider(SparkContextTestProvider.class);
    }
}
