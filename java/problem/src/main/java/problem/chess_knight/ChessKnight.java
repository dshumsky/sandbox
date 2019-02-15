package problem.chess_knight;

import org.javatuples.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import static java.lang.Math.abs;

public class ChessKnight {

    public int solution(int A, int B) {
        return solution(A, B, 0, 0);
    }

    Map<Pair<Integer, Integer>, Integer> prevStepMap = new HashMap<>();
    Map<Pair<Integer, Integer>, Integer> stepMap = new HashMap<>();
    Map<Pair<Integer, Integer>, Integer> slowMap = new HashMap<>();

    public int slow(int A, int B) {
        if (slowMap.isEmpty()) {
            slowMap.put(Pair.with(0, 0), 0);
            prevStepMap.put(Pair.with(0, 0), 0);
        }
        Pair<Integer, Integer> key = Pair.with(A, B);
        while (slowMap.get(key) == null) {
            iteration();
        }
        int result = slowMap.get(key);
        if (result > 100000) {
            result = -2;
        }
        return result;
    }

    private void iteration() {
        stepMap.clear();
        for (Entry<Pair<Integer, Integer>, Integer> e : prevStepMap.entrySet()) {
            process(Pair.with(e.getKey().getValue0() + 1, e.getKey().getValue1() + 2), e.getValue() + 1);
            process(Pair.with(e.getKey().getValue0() + 1, e.getKey().getValue1() - 2), e.getValue() + 1);
            process(Pair.with(e.getKey().getValue0() - 1, e.getKey().getValue1() + 2), e.getValue() + 1);
            process(Pair.with(e.getKey().getValue0() - 1, e.getKey().getValue1() - 2), e.getValue() + 1);
            process(Pair.with(e.getKey().getValue0() + 2, e.getKey().getValue1() + 1), e.getValue() + 1);
            process(Pair.with(e.getKey().getValue0() + 2, e.getKey().getValue1() - 1), e.getValue() + 1);
            process(Pair.with(e.getKey().getValue0() - 2, e.getKey().getValue1() + 1), e.getValue() + 1);
            process(Pair.with(e.getKey().getValue0() - 2, e.getKey().getValue1() - 1), e.getValue() + 1);
        }
        prevStepMap.clear();
        prevStepMap.putAll(stepMap);
    }

    private void process(Pair<Integer, Integer> key, int newValue) {
        if (!slowMap.containsKey(key)) {
            slowMap.put(key, newValue);
            stepMap.put(key, newValue);
        }
    }

    public int solution(int A, int B, int steps, int iterations) {
        A = Math.abs(A);
        B = Math.abs(B);
        if (abs(A) > abs(B)) {
            int C = A;
            A = B;
            B = C;
        }
        // now abs(A) <= abs(B)
        int result;
        if (iterations > 40) {
            result = -1;
        } else if (abs(A) <= 11 && abs(B) <= 11) {
            result = steps + slow(A, B);
        } else if (2 * abs(A) == abs(B)) {
            result = steps + abs(A);
        } else if (A == 0 && abs(B) % 4 == 0) {
            result = steps + abs(B) / 2;
        } else if (A == 0 && abs(B) / 4 >= 1) {
            int step4x = (abs(B) - (abs(B) % 4)) / 4 - 1;
            result = solution(abs(B) - 4 * step4x, 0, steps + step4x * 2, iterations + 1);
        } else if (2 * abs(A) < abs(B) && abs(A) > 4) {
            result = solution(abs(B) - 2 * (abs(A) - 4), 0, steps + (abs(A) - 4), iterations + 1);
        } else if (2 * abs(A) < abs(B) && abs(A) <= 4) {
            int step4x = (abs(B) - (abs(B) % 4)) / 4 - 2;
            result = solution(abs(B) - 4 * step4x, A, steps + step4x * 2, iterations + 1);
        } else {
            // 2x + y = A, 2y + x = B => x= (2A-B)/3, y= (2B-A)/3
            int x = (int) Math.round((2 * abs(A) - abs(B)) / 3d) - 2;
            int y = (int) Math.round((2 * abs(B) - abs(A)) / 3d) - 2;
            result = solution(abs(B) - 2 * y - x, A - 2 * x - y, steps + x + y, iterations + 1);
        }

        if (result > 100000) {
            result = -2;
        }
        return result;
    }
}
