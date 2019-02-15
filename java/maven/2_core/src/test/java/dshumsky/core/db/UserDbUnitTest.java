package dshumsky.core.db;

import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.UsingSteps;
import org.jbehave.core.annotations.guice.UsingGuice;
import org.jbehave.core.junit.guice.GuiceAnnotatedEmbedderRunner;
import org.junit.runner.RunWith;
import org.testng.Assert;

import com.google.inject.Inject;
import dshumsky.core.reactjsexample.api.UserDao;
import dshumsky.core.tests.jbehave.JBehaveStory;

/**
 * @author dshumski (Dmitry.Shumski@mgm-tp.com)
 */
@RunWith(GuiceAnnotatedEmbedderRunner.class)
@UsingGuice(modules = { DbTestModule.class })
@UsingSteps(instances = { DbStep.class, UserDbUnitTest.class })
public class UserDbUnitTest extends JBehaveStory {

    @Inject
    private UserDao userDao;

    @Then("test")
    public void test() {
        Assert.assertEquals(userDao.findAll().size(), 5);
    }
}
