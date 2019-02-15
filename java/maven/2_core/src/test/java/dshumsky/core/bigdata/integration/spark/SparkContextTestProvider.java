package dshumsky.core.bigdata.integration.spark;

import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.apache.commons.lang3.concurrent.LazyInitializer;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

import com.google.inject.Provider;

/**
 * @author Dmitry Shumski (dshumsky@gmail.com)
 */
public class SparkContextTestProvider implements Provider<JavaSparkContext> {

    private final LazyInitializer<JavaSparkContext> lazyInitializer = new LazyInitializer<JavaSparkContext>() {
        @Override
        protected JavaSparkContext initialize() throws ConcurrentException {
            SparkConf conf = new SparkConf();
            return new JavaSparkContext("local", "Test Context", conf);
        }
    };

    @Override
    public JavaSparkContext get() {
        try {
            return lazyInitializer.get();
        } catch (ConcurrentException e) {
            throw new RuntimeException();
        }
    }
}
