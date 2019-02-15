package dshumsky.core.bigdata.spark;

import org.apache.spark.api.java.JavaSparkContext;

/**
 * @author Dmitry Shumski (dshumsky@gmail.com)
 */
public interface SparkContextFactory {
    JavaSparkContext create();
}
