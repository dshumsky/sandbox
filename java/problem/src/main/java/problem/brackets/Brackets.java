package problem.brackets;

public class Brackets {

    public int solution(String s) {
        int sumClosing = 0;
        for (int i = 0; i < s.length(); i++) {
            sumClosing += closing(s.charAt(i));
        }

        if (sumClosing == 0) {
            return 0;
        }

        int sumKOpening = 0;
        int sumKClosing = 0;

        for (int k = 1; k <= s.length(); k++) {
            sumKOpening += opening(s.charAt(k - 1));
            sumKClosing += closing(s.charAt(k - 1));
            if (sumKOpening == (sumClosing - sumKClosing)) {
                return k;
            }
        }

        return -1; // should never happen
    }

    private int opening(char c) {
        return c == '(' ? 1 : 0;
    }

    private int closing(char c) {
        return c == ')' ? 1 : 0;
    }
}
