package problem.domino;

import com.google.common.collect.ImmutableList;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DominoTest {
    @Test
    public void test() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        String filePath = DominoTest.class.getPackage().getName().replace('.', '/');
        File file = new File(classLoader.getResource(filePath + "/test.txt").getFile());
        List<Integer> longest = Domino.longest(file);
        Assert.assertEquals(ImmutableList.of(1, 1, 3, 1, 4, 7), longest);
    }
}
