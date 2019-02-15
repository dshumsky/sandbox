package problem.chess_knight;

import org.jbehave.core.annotations.Configure;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.UsingEmbedder;
import org.jbehave.core.annotations.UsingSteps;
import org.jbehave.core.failures.FailingUponPendingStep;
import org.jbehave.core.io.CasePreservingResolver;
import org.jbehave.core.junit.AnnotatedEmbedderRunner;
import org.jbehave.core.junit.JUnitStory;
import org.jbehave.core.model.ExamplesTable;
import org.junit.runner.RunWith;
import org.testng.asserts.SoftAssert;
import org.testng.internal.collections.Pair;
import problem.JBehaveTableCustomizableOutput;

import java.util.function.Function;

import static java.lang.Integer.parseInt;
import static java.lang.System.currentTimeMillis;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@RunWith(AnnotatedEmbedderRunner.class)
@Configure(pendingStepStrategy = FailingUponPendingStep.class, storyPathResolver = CasePreservingResolver.class)
@UsingEmbedder(verboseFailures = true)
@UsingSteps(instances = { ChessKnightTest.class })
public class ChessKnightTest extends JUnitStory {

    @Then("solution for A,B is $table")
    public void test(ExamplesTable table) {

        ChessKnight solution3 = new ChessKnight();

        SoftAssert anAssert = new SoftAssert();
        // test
        test(table, anAssert, pair -> solution3.solution(pair.first(), pair.second()), pair -> "" + pair.first() + "(" + (pair.second() > 0 ? "+" : "") + pair.second() + ")");
        anAssert.assertAll();

        // print
        // test(table, anAssert, pair -> solution3.slow(pair.first(), pair.second()), pair -> ""+pair.first() + "(" + pair.second()+")");
    }

    @Then("test all A=[$a1, $a2] and B=[$b1, $b2]")
    public void test(int a1, int a2, int b1, int b2) {
        ChessKnight solution3 = new ChessKnight();

        for (int a = a1; a <= a2; a++) {
            for (int b = b1; b <= b2; b++) {
                int expected = solution3.slow(a, b);
                int actual = solution3.solution(a, b);
                assertEquals(actual, expected);
            }
        }
    }

    @Then("performance: A=[$a1, $a2] and B=[$b1, $b2] takes less then $timeLimit ms")
    public void performance(int a1, int a2, int b1, int b2, long timeLimit) {
        long start = currentTimeMillis();
        ChessKnight solution3 = new ChessKnight();

        for (int a = a1; a <= a2; a++) {
            for (int b = b1; b <= b2; b++) {
                solution3.solution(a, b);
            }
            assertTrue((currentTimeMillis() - start) < timeLimit);
        }
        long actualTime = currentTimeMillis() - start;
        assertTrue(actualTime < timeLimit);
        System.out.println("actualTime=" + actualTime);
    }

    public void test(ExamplesTable table, SoftAssert anAssert, Function<Pair<Integer, Integer>, Integer> solutionFunction, Function<Pair<Integer, Integer>, String> printFunction) {
        JBehaveTableCustomizableOutput handler = new JBehaveTableCustomizableOutput(table) {
            @Override
            protected String transformCell(int rowIndex, String cellValue, String header) {
                if (header.length() == 0) {
                    return cellValue;
                } else {
                    int a = parseInt(table.getRow(rowIndex).get(""));
                    int b = parseInt(header);
                    int actual = solutionFunction.apply(Pair.of(a, b));
                    if (cellValue.length() == 0) {
                        return "" + actual;
                    } else {
                        anAssert.assertEquals(actual, parseInt(cellValue));
                        int expected = parseInt(cellValue);
                        if (actual != parseInt(cellValue)) {
                            return printFunction.apply(Pair.of(expected, actual));
                        } else {
                            return cellValue;
                        }
                    }
                }
            }
        };
        System.out.println(handler.toString());
    }

}
