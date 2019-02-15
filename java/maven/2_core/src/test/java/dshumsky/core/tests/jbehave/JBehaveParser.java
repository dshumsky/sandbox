package dshumsky.core.tests.jbehave;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.steps.ParameterConverters;

import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * This class contains static utility methods to parse JBehave tests
 */
public class JBehaveParser {

    static final String NULL = "<null>";

    private static final String ANY = "*ANY*";
    private static final String NOT = "!";
    public static Set<Boolean> TrueFalse = ImmutableSet.of(true, false);

    /**
     * Function to parse {@link Boolean}
     */
    public final static Function<String, Boolean> P_BOOLEAN = new Function<String, Boolean>() {
        private final Set<String> TRUE_VALUES = Sets.newHashSet("Y", "y", "true", "TRUE", "1");
        private final Set<String> FALSE_VALUES = Sets.newHashSet("N", "n", "true", "FALSE", "0", "-");

        public Boolean apply(String value) {
            if (value == null || value.isEmpty() || value.equals(NULL)) {
                return null;
            } else if (TRUE_VALUES.contains(value)) {
                return true;
            } else if (FALSE_VALUES.contains(value)) {
                return false;
            } else {
                throw new IllegalArgumentException(value);
            }
        }
    };

    /**
     * Function to parse {@link Integer}
     */
    public final static Function<String, Integer> P_INTEGER = new Function<String, Integer>() {
        public Integer apply(String value) {
            if (value == null || value.isEmpty() || value.equals(NULL)) {
                return null;
            } else {
                return Integer.parseInt(value);
            }
        }
    };

    /**
     * Function to parse {@link BigDecimal}
     */
    public final static Function<String, BigDecimal> P_BIGDECIMAL = new Function<String, BigDecimal>() {
        public BigDecimal apply(String value) {
            if (value == null || value.isEmpty() || value.equals(NULL) || value.equals("-")) {
                return null;
            } else {
                return new BigDecimal(value);
            }
        }
    };

    /**
     * Parse a {@link HashBasedTable} from {@link ExamplesTable}
     * 
     * @param table {@link ExamplesTable}
     * @param headerKeys map of row keys
     * @param columnKeys map of column keys
     * @param converter converter for value
     * @return {@link HashBasedTable}
     */
    public static <R, C, V> HashBasedTable<R, C, V> parseTable(ExamplesTable table, Map<String, R> headerKeys, Map<String, C> columnKeys,
            Function<String, V> converter) {
        int headerSize = table.getHeaders().size();
        assert headerSize > 1;
        HashBasedTable<R, C, V> result = HashBasedTable.create(headerSize - 1, table.getRowCount());
        for (int i = 0; i < table.getRowCount(); i++) {
            Map<String, String> row = table.getRow(i);
            R rowKey = headerKeys.get(row.get(""));
            assert rowKey != null;
            for (Map.Entry<String, String> e : row.entrySet()) {
                if (e.getKey().equals("")) {
                    continue;
                }
                C columnKey = columnKeys.get(e.getKey());
                assert columnKey != null;
                V value = converter.apply(e.getValue());
                result.put(rowKey, columnKey, value);
            }
        }
        return result;
    }

    /**
     * Parse a {@link Map} from {@link ExamplesTable}
     * 
     * @param table {@link ExamplesTable}
     * @param type the type of mapped values
     * @return {@link Map}
     */
    public static <V> Map<String, V> parseMap(ExamplesTable table, Class<? extends V> type) {
        ParameterConverters converters = new ParameterConverters();
        HashMap<String, V> map = new HashMap<>(table.getRowCount());
        for (Map<String, String> row : table.getRows()) {
            @SuppressWarnings("unchecked")
            V value = (V) converters.convert(row.get("value"), type);
            String key = row.get("key");
            map.put(key, value);
        }
        return map;
    }

    /**
     * Parse a {@link Set} from string by {@link Map}
     * 
     * @param keys coma-separated values
     * @param map the map of values
     */
    public static <V> Set<V> parseSet(String keys, Map<String, V> map) {
        Set<V> result = Sets.newHashSet();
        for (String key : Splitter.on(',').omitEmptyStrings().trimResults().split(keys)) {
            if (key.equals(ANY)) {
                result.addAll(map.values());
            } else if (key.startsWith(NOT)) {
                V v = map.get(key.substring(1));
                assert v != null;
                result.addAll(map.values());
                result.remove(v);
            } else if (key.equals(NULL)) {
                result.add(null);
            } else {
                V v = map.get(key);
                assert v != null;
                result.add(v);
            }
        }
        return result;
    }

    /**
     * Parse a {@link List} from string by {@link Map}
     * 
     * @param keys coma-separated values
     * @param map the map of values
     */
    public static <V> List<V> parseList(String keys, Map<String, V> map) {
        return parseList(keys, map, ',');
    }

    /**
     * Parse a {@link Set} from string by {@link Map}
     * 
     * @param keys separated values
     * @param separator the separator
     * @param map the map of values
     */
    public static <V> List<V> parseList(String keys, Map<String, V> map, char separator) {
        List<V> result = new ArrayList<>();
        for (String key : Splitter.on(separator).omitEmptyStrings().trimResults().split(keys)) {
            V v = map.get(key);
            assert v != null;
            result.add(v);
        }
        return result;
    }

    /**
     * Parse a {@link List} of booleans from string
     * 
     * @param keys coma-separated values
     */
    public static List<Boolean> parseListOfBoolean(String keys) {
        return parseListOfBoolean(keys, ',');
    }

    /**
     * Parse a {@link List} of booleans from string
     * 
     * @param keys coma-separated values
     * @param separator the separator
     */
    public static List<Boolean> parseListOfBoolean(String keys, char separator) {
        List<Boolean> result = new ArrayList<>();
        parseCollectionOfBoolean(keys, result, separator);
        return result;
    }

    /**
     * Parse a {@link Set} of booleans from string
     * 
     * @param keys coma-separated values
     */
    public static Set<Boolean> parseSetOfBoolean(String keys) {
        HashSet<Boolean> result = new HashSet<>(3);
        parseCollectionOfBoolean(keys, result, ',');
        return result;
    }

    private static void parseCollectionOfBoolean(String keys, Collection<Boolean> result, char separator) {
        for (String key : Splitter.on(separator).omitEmptyStrings().trimResults().split(keys)) {
            if (ANY.equals(key)) {
                result.add(true);
                result.add(false);
            } else {
                result.add(P_BOOLEAN.apply(key));
            }
        }
    }

    /**
     * Parse a {@link List} of Dates from string
     * 
     * @param dates the string with dates
     * @param separator the separator
     */
    public static List<Date> parseDateList(String dates, char separator) {
        Iterable<String> dateStrings = Splitter.on(separator).omitEmptyStrings().trimResults().split(dates);
        final ParameterConverters converters = new ParameterConverters();
        return Lists.newArrayList(Iterables.transform(dateStrings, new Function<String, Date>() {
            @Override
            public Date apply(String input) {
                return (Date) converters.convert(input, Date.class);
            }
        }));
    }

    /**
     * @param value - cell in JBehave table
     * @return true, if the value in the cell is considered to be null
     */
    public static boolean isNull(String value) {
        return NULL.equals(value);
    }
}