package problem.brackets;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class BracketsTest {

    @DataProvider
    public Object[][] dataProvider() {
        return new Object[][] {
                { "(())))(", 4 },
                { "", 0 },
                { "()", 1 },
                { "(())", 2 },
                { ")()", 2 },
                { ")()(", 2 },
                { "))()(", 3 },
                { ")(((())", 3 },
                { "()()", 2 },
                { "()()()", 3 },
                { "()()()()", 4 },
                { "()(())()", 4 },
                { ")", 1 },
                { "))", 2 },
                { ")))", 3 },
                { ")))()", 4 },
                { ")))()(((", 4 },
                { ")))()()(((", 5 },
                { ")))(((())", 5 },
                { ")))(((()))", 6 }
        };
    }

    @Test(dataProvider = "dataProvider")
    public void test(String s, int expected) {
        Brackets solution = new Brackets();
        int actual = solution.solution(s);
        assertEquals(actual, expected);
    }
}
