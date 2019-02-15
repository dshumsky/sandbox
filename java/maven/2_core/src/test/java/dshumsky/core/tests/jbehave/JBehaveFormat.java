package dshumsky.core.tests.jbehave;

import static com.google.common.base.Strings.padEnd;

import java.util.Collection;
import java.util.Date;

import org.javatuples.Quartet;
import org.javatuples.Quintet;
import org.javatuples.Triplet;
import org.jbehave.core.steps.ParameterConverters.DateConverter;

/**
 * This class contains static utility methods to format output for objects used in JBehave tests
 */
public class JBehaveFormat {

    private JBehaveFormat() {
    }

    /**
     * Returns a formatted string using the specified format string and arguments
     * 
     * @param format format string
     * @param arguments {@link Triplet}
     * @return formatted string
     */
    public static String format(String format, Triplet<?, ?, ?> arguments) {
        return String.format(format, f(arguments.getValue0()), f(arguments.getValue1()), f(arguments.getValue2()));
    }

    /**
     * Returns a formatted string using the specified format string and arguments
     * 
     * @param format format string
     * @param arguments {@link Triplet}
     * @return formatted string
     */
    public static String format(String format, Quartet<?, ?, ?, ?> arguments) {
        return String.format(format, f(arguments.getValue0()), f(arguments.getValue1()), f(arguments.getValue2()), f(arguments.getValue3()));
    }

    /**
     * Returns a formatted string using the specified format string and arguments
     * 
     * @param format format string
     * @param arguments {@link Triplet}
     * @return formatted string
     */
    public static String format(String format, Quintet<?, ?, ?, ?, ?> arguments) {
        return String.format(format, f(arguments.getValue0()), f(arguments.getValue1()), f(arguments.getValue2()), f(arguments.getValue3()), f(arguments
                .getValue4()));
    }

    /**
     * Returns a formatted string by object
     */
    public static String format(Object s) {
        if (s!=null) {
            if (Date.class.isAssignableFrom(s.getClass())) {
                return DateConverter.DEFAULT_FORMAT.format((Date)s);
            } else {
                return s.toString();
            }
        } else {
            return JBehaveParser.NULL;
        }
    }

    /**
     * Returns a formatted string corresponding to a row of JBehave table
     * 
     * @param values to be formatted
     * @param padEnds end padding for values. Optional.
     * @return formatted string
     */
    public static String formatRow(Collection<?> values, int... padEnds) {
        StringBuilder s = new StringBuilder("|");
        int i = 0;
        int padEnd = 10; //default
        for (Object v : values) {
            padEnd = (i < padEnds.length ? padEnds[i] : padEnd);
            s.append(padEnd(f(v), padEnd, ' ')).append("|");
            i++;
        }
        return s.toString();
    }

    private static String f(Object s) {
        return format(s);
    }

}
