package java.problem.c;

import java.util.HashMap;
import java.util.Set;

/**
 *
 */
public class Main {

    public static void main(String[] args) {
        HashMap<String, Set<String>> m = new HashMap<>();
        for (i=0; i<16; i++) {
            System.out.println(str(i, 4));
        }

    }

    static String inc(String s) {
        for (int i = 0; i < s.length(); i++) {

        }
    }

    static String str(int i, int n) {
        String result = "";
        for (j = 0; j < n; j++) {
            result = i / Math.pow(j) % 2 == 0 ? "0" : "1" + result;
        }
    }

    static int diff(String x, String y) {
        result = 0;
        for (i = 0; i < x.; j++) {
            result = i / Math.pow(j) % 2 == 0 ? "0" : "1" + result;
        }
    }
}
