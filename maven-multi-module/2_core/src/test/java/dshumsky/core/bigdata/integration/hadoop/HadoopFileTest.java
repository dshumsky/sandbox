package dshumsky.core.bigdata.integration.hadoop;

import org.jbehave.core.annotations.UsingSteps;
import org.jbehave.core.annotations.guice.UsingGuice;
import org.jbehave.core.junit.guice.GuiceAnnotatedEmbedderRunner;
import org.junit.runner.RunWith;

import dshumsky.core.bigdata.integration.hadoop.step.HDFSOperations;
import dshumsky.core.tests.jbehave.JBehaveStory;

/**
 * @author Dmitry Shumski (dshumsky@gmail.com)
 */
@RunWith(GuiceAnnotatedEmbedderRunner.class)
@UsingGuice(modules = {HadoopTestModule.class})
@UsingSteps(instances = {HDFSOperations.class})
public class HadoopFileTest  extends JBehaveStory
{
    public HadoopFileTest() {
    }
}
