package dshumsky.core.tests.jbehave;

import java.util.Collections;
import java.util.List;

import org.jbehave.core.annotations.Configure;
import org.jbehave.core.annotations.UsingEmbedder;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.failures.FailingUponPendingStep;
import org.jbehave.core.io.CasePreservingResolver;
import org.jbehave.core.junit.JUnitStory;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.junit.Test;

/**
 * {@link JUnitStory} with default configuration
 */
@Configure(pendingStepStrategy = FailingUponPendingStep.class, failureStrategy = FailingUponPendingStep.class, storyPathResolver = CasePreservingResolver.class)
@UsingEmbedder(embedder = Embedder.class, verboseFailures = true)
public class JBehaveStory extends JUnitStory {

    public JBehaveStory() {
    }

    @Override
    public InjectableStepsFactory stepsFactory() {
        return new InstanceStepsFactory(configuration(), getSteps());
    }

    /**
     * Returns steps for story. Needs to be overridden if necessary.
     * 
     * @return steps for story
     */
    protected List<?> getSteps() {
        return Collections.singletonList(this);
    }

    @Test
    public void run() throws Throwable {
        super.run();
    }
}
