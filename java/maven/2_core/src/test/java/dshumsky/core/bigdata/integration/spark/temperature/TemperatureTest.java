package dshumsky.core.bigdata.integration.spark.temperature;

import java.io.IOException;
import java.util.Map;

import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.UsingSteps;
import org.jbehave.core.annotations.guice.UsingGuice;
import org.jbehave.core.junit.guice.GuiceAnnotatedEmbedderRunner;
import org.jbehave.core.model.ExamplesTable;
import org.junit.runner.RunWith;

import com.google.inject.Inject;
import dshumsky.core.bigdata.hadoop.HadoopFile;
import dshumsky.core.bigdata.hadoop.HadoopFileFactory;
import dshumsky.core.bigdata.integration.hadoop.HadoopTestModule;
import dshumsky.core.bigdata.integration.hadoop.step.HDFSOperations;
import dshumsky.core.bigdata.integration.spark.SparkTestModule;
import dshumsky.core.bigdata.spark.temperature.TemperatureAggregated;
import dshumsky.core.bigdata.spark.temperature.TemperatureAggregatedFactory;
import dshumsky.core.bigdata.spark.temperature.TemperatureByTimeAggregation;
import dshumsky.core.bigdata.spark.temperature.TemperatureModule;
import dshumsky.core.tests.jbehave.JBehaveStory;
import dshumsky.core.tests.jbehave.JBehaveTableAssert;
import dshumsky.core.tests.jbehave.JBehaveTableAssert.PrintCellIfErrorStrategy;

/**
 * @author Dmitry Shumski (dshumsky@gmail.com)
 */
@RunWith(GuiceAnnotatedEmbedderRunner.class)
@UsingGuice(modules = {HadoopTestModule.class, SparkTestModule.class, TemperatureModule.class})
@UsingSteps(instances = {HDFSOperations.class, TemperatureTest.class})
public class TemperatureTest extends JBehaveStory {

    @Inject
    private HadoopFileFactory fileFactory;

    @Inject
    private TemperatureAggregatedFactory temperatureAggregatedFactory;

    @Then("aggregationByTime for $path is $table")
    public void aggregationByTime(String path, ExamplesTable table) throws IOException {
        HadoopFile file = fileFactory.create(path);
        TemperatureAggregated temperatureAggregated = temperatureAggregatedFactory.create(file);
        Map<Long, TemperatureByTimeAggregation> actual = temperatureAggregated.aggregationByTime();
        JBehaveTableAssert tableAssert = new JBehaveTableAssert(table);
        tableAssert.assertEqualsTo(actual, "-", PrintCellIfErrorStrategy.EXPECTED_AND_ACTUAL);
    }
}
