package dshumsky.core.tests.base;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * A collection of frequently used/required functions on numbers in this project.
 *
 * @author Dmitry Shumski (dshumsky@gmail.com)
 */
public class Numbers {
    /**
     * Compares two BigDecimals in the smaller of their scales. In effect compares all digits available to both numbers, ignores the rest.
     */
    public static boolean equalsMoreOrLess(BigDecimal first, BigDecimal second) {
        if (first == null) {
            return second == null;
        }
        if (second == null) {
            return false;
        }
        if (first.scale() < second.scale()) {
            return first.compareTo(second.setScale(first.scale(), RoundingMode.HALF_UP)) == 0;
        } else {
            return second.compareTo(first.setScale(second.scale(), RoundingMode.HALF_UP)) == 0;
        }
    }

}
