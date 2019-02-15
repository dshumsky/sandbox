package problem.number_system;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NumberSystemTest {

    private ArrayList<Integer> list = new ArrayList<>();

    @DataProvider
    public Iterator<Object[]> dataProvider() {
        ArrayList<Object[]> result = new ArrayList<>();
        for (int x = 0; x < 1000; x++) {
            int[] array = toArray(x);
            result.add(new Object[] { array });
        }
        return result.iterator();
    }

    private int[] toArray(int x) {
        if (x == 0)
            return new int[0];
        list.clear();
        int i = x;
        while (i > 0) {
            list.add(i % 2);
            i = i / 2;
        }
        return list.stream().mapToInt(Integer::intValue).toArray();
    }

    private int toInt(int[] A) {
        if (A.length == 0)
            return 0;
        int res = A[0];
        int power = -2;
        for (int i = 1; i < A.length; i++) {
            res += A[i] * power;
            power *= (-2);
        }
        return res;
    }

    @Test(dataProvider = "dataProvider")
    public void test(int[] A) {
        NumberSystem solution = new NumberSystem();
        int[] B = solution.solution(A);
        Assert.assertEquals(toInt(solution.solution(A)), -toInt(A));
        for (int b : B) {
            Assert.assertTrue(b == 0 || b == 1);
        }
    }

    private String asString(int[] A) {
        return String.join(",", IntStream.of(A).boxed().map(i -> "" + i).collect(Collectors.toList()));
    }
}
