package dshumsky.core.tests.testng;

import java.math.BigDecimal;

import org.testng.Assert;

import dshumsky.core.tests.base.Numbers;

import static org.testng.internal.EclipseInterface.ASSERT_LEFT;
import static org.testng.internal.EclipseInterface.ASSERT_MIDDLE;
import static org.testng.internal.EclipseInterface.ASSERT_RIGHT;

/**
 * Extension of {@link org.testng.Assert}
 *
 * @author Dmitry Shumski (dshumsky@gmail.com)
 */
public class ExtendedAssert extends Assert {

    protected ExtendedAssert() {
    }

    /**
     * Asserts that actual equals to expected comparing values in the smaller of their scales (in effect compares all digits available to both numbers, ignores the rest).
     *
     * @param actual - the actial value
     * @param expected - the expected value
     */
    public static void assertEqualsMoreOrLess(BigDecimal actual, BigDecimal expected) {
        assertEqualsMoreOrLess(actual, expected, null);
    }

    /**
     * Asserts that actual equals to expected comparing values in the smaller of their scales (in effect compares all digits available to both numbers, ignores the rest).
     *
     * @param actual - the actial value
     * @param expected - the expected value
     * @param message - the error message
     */
    public static void assertEqualsMoreOrLess(BigDecimal actual, BigDecimal expected, String message) {
        if ((expected == null) && (actual == null)) {
            return;
        }
        if (expected != null) {
            if (Numbers.equalsMoreOrLess(expected, actual)) {
                return;
            }
        }
        fail(format(actual, expected, message));
    }

    static String format(Object actual, Object expected, String message) {
        String formatted = "";
        if (null != message) {
            formatted = message + " ";
        }

        return formatted + ASSERT_LEFT + expected + ASSERT_MIDDLE + actual + ASSERT_RIGHT;
    }

}
