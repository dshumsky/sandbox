package problem.closest_greater_element;

import org.jbehave.core.annotations.Configure;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.UsingEmbedder;
import org.jbehave.core.annotations.UsingSteps;
import org.jbehave.core.annotations.When;
import org.jbehave.core.failures.FailingUponPendingStep;
import org.jbehave.core.io.CasePreservingResolver;
import org.jbehave.core.junit.AnnotatedEmbedderRunner;
import org.jbehave.core.junit.JUnitStory;
import org.junit.runner.RunWith;

import java.util.List;

import static org.testng.Assert.assertEquals;

@RunWith(AnnotatedEmbedderRunner.class)
@Configure(pendingStepStrategy = FailingUponPendingStep.class, storyPathResolver = CasePreservingResolver.class)
@UsingEmbedder(verboseFailures = true)
@UsingSteps(instances = { ClosestGreaterElementTest.class })
public class ClosestGreaterElementTest extends JUnitStory {

    private List<Integer> a;
    private List<Integer> b;

    @When("a[]= $a and b[]= $b")
    public void a_b(List<Integer> a, List<Integer> b) {
        this.a = a;
        this.b = b;
    }

    @Then("с[]= $с")
    public void thenC(List<Integer> c) {
        List<Integer> actual = new ClosestGreaterElement(a, b).c();
        assertEquals(actual, c, "actual=" + actual);
    }

}
