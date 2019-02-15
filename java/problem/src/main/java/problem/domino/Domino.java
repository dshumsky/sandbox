package problem.domino;

import com.google.common.base.Splitter;
import org.javatuples.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.lang.Integer.parseInt;

public class Domino {
    /**
     * "Domino": We are given an S string, representing a domino tile chain.
     * Each tile has an L-R format, where L and R are numbers from the 1..6 range.
     * The tiles are separated by a comma. Some examples of a valid S chain are:
     * 1. "6-3"
     * 2. "4-3,5-1,2-2,1-3,4-4"
     * 3. "1-1,3-5,5-2,2-3,2-4"
     * Devise a function that will read from the given file line by line,
     * where each line represents an input S string, and will return the number of tiles in the longest matching
     * group within each input S string. A matching group is a set of tiles that match and that are subsequent in S.
     * Domino tiles match if the right side of a tile is the same as
     * the left side of the following tile. 2-4,4-1 are matching tiles, but 5-2,1-6 are not.
     * Function domino() should read file s.txt and return [1, 1, 3].
     */

    public static List<Integer> longest(File file) throws IOException {
        ArrayList<Integer> result = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                result.add(processLine(line));
            }
        }
        return result;
    }

    private static int processLine(String line) {
        Iterable<String> split = Splitter.on(",").split(line);
        Pair<Integer, Integer> prev = null;
        int subseq = 0;
        int max = 0;
        for (String s : split) {
            Iterator<String> strings = Splitter.on("-").split(s).iterator();
            Pair<Integer, Integer> current = Pair.with(parseInt(strings.next()), parseInt(strings.next()));
            if (prev == null) {
                subseq++;
            } else if (prev.getValue1().equals(current.getValue0())) {
                subseq = Math.max(2, subseq + 1);
            } else {
                max = Math.max(max, subseq);
                subseq = 0;
            }
            prev = current;
        }
        max = Math.max(max, subseq);
        return max;
    }
}
