package problem.number_system;

import java.util.ArrayList;
import java.util.Collections;

public class NumberSystem {
    public int[] solution(int[] A) {
        if (A.length == 0)
            return new int[0];

        ArrayList<Integer> R = new ArrayList<>(Collections.nCopies(A.length + 2, 0));
        for (int i = 0; i < R.size(); i++) {
            if (i < A.length && A[i] == 1) {
                R.set(i, R.get(i) + 1);
                R.set(i + 1, R.get(i + 1) + 1);
            }
            if (R.get(i) == 2) {
                if (R.get(i + 1) > 0) {
                    R.set(i + 1, R.get(i + 1) - 1);
                } else {
                    R.set(i + 1, R.get(i + 1) + 1);
                    R.set(i + 2, R.get(i + 2) + 1);
                }
                R.set(i, 0);
            }
        }

        while (R.get(R.size() - 1) == 0) {
            R.remove(R.size() - 1);
        }

        return R.stream().mapToInt(Integer::intValue).toArray();
    }
}
