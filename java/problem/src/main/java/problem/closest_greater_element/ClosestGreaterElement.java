package problem.closest_greater_element;

import java.util.ArrayList;
import java.util.List;

/**
 * Given two arrays a[] and b[], we need to build an array c[]
 * such that every element c[i] of c[] contains a value from a[]
 * which is greater than b[i] and is closest to b[i].
 * If a[] has no greater element than b[i], then value of c[i] is -1.
 * All arrays are of same size.
 */
public class ClosestGreaterElement {

    private final List<Integer> a;
    private final List<Integer> b;

    public ClosestGreaterElement(final List<Integer> a, final List<Integer> b) {
        this.a = a;
        this.b = b;
    }

    public List<Integer> c() {
        int n = a.size();
        ArrayList<Integer> c = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            c.add(closestGreater(a, b.get(i)));
        }
        return c;
    }

    private int closestGreater(final List<Integer> a, final int b) {
        int result = -1;
        for (Integer ai : a) {
            if (ai > b && (result == -1 || ai < result)) {
                result = ai;
            }
        }
        return result;
    }
}
