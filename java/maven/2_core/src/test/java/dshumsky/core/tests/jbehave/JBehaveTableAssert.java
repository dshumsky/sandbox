package dshumsky.core.tests.jbehave;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.steps.ParameterConverters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.ImmutableTable.Builder;

import dshumsky.core.tests.base.Numbers;

/**
 * Assertion tool class for JBehave-table. Presents assertion methods to compare table with java-objects.
 * If value in cell starts with '~' (e.x. '~20.56') then {@link Numbers#equalsMoreOrLess} will be used for comparison.
 *
 * @author Dmitry Shumski (dshumsky@gmail.com)
 */
public class JBehaveTableAssert {

    /**
     * Strategy how to print cell when actual value differs from expected
     */
    public enum PrintCellIfErrorStrategy {
        /**
         * prints in format 'expected (actual)'
         */
        EXPECTED_AND_ACTUAL,
        /**
         * prints in format 'actual'
         */
        ACTUAL
    }

    private static final Logger logger = LoggerFactory.getLogger(JBehaveTableAssert.class);
    private static final String MORE_OR_LESS_PREFIX = "~";

    private final ExamplesTable table;
    private final PropertyUtilsBean propertyUtilsBean;
    private final ParameterConverters parameterConverters;

    /**
     * Constructor
     *
     * @param table - the JBehave table
     */
    public JBehaveTableAssert(ExamplesTable table) {
        this.table = table;
        this.propertyUtilsBean = new PropertyUtilsBean();
        this.parameterConverters = new ParameterConverters();
    }

    /**
     * Asserts that collection has the same size as number of rows in table
     *
     * @param collection - the collection to compare
     */
    public void assertSameSize(Collection<?> collection) {
        Assert.assertEquals(collection.size(), table.getRowCount(), "sizes are different");
    }

    /**
     * Compares each cell in table with a property-value of an object in the list
     *
     * @param list - the list to compare
     */
    public void assertEqualsTo(List<?> list) {
        assertEqualsTo(list, null, PrintCellIfErrorStrategy.EXPECTED_AND_ACTUAL);
    }

    /**
     * Compares each cell (except column with header name equals '') in table with a property-value of an object in the map
     * Column with empty header is used as a key
     *
     * @param map - the map to compare
     * @param skipToCompareCellValue - if a cell in the table contains this value, it will be skipped from comparison.
     *            If it's null then all cells will be compared
     * @param printCellIfErrorStrategy - the strategy how to print cell when actual value differs from expected. See {@link PrintCellIfErrorStrategy} for details
     */
    public void assertEqualsTo(Map<?, ?> map, @Nullable String skipToCompareCellValue, final PrintCellIfErrorStrategy printCellIfErrorStrategy) {
        assertSameSize(map.keySet());
        Assert.assertTrue(table.getHeaders().contains(""), "Table should contain empty header as a key for Map");
        Map<String, Object> mapWithStringKey = new HashMap<>();
        for (Entry<?, ?> e : map.entrySet()) {
            mapWithStringKey.put(e.getKey().toString(), e.getValue());
        }
        assertEqualsTo(printCellIfErrorStrategy,
                row -> {
                    String key = row.values.get("");
                    Object value = mapWithStringKey.get(key);
                    Assert.assertNotNull(value, "Map should contain key=" + key);
                    return value;
                },
                cell -> (cell.getKey().length() > 0)
                        && (skipToCompareCellValue == null || !skipToCompareCellValue.equals(cell.getValue())));
    }

    /**
     * Compares each cell in table with a property-value of an object in the list
     *
     * @param list - the list to compare
     * @param skipToCompareCellValue - if a cell in the table contains this value, it will be skipped from comparison.
     *            If it's null then all cells will be compared
     * @param printCellIfErrorStrategy - the strategy how to print cell when actual value differs from expected. See {@link PrintCellIfErrorStrategy} for details
     */
    public void assertEqualsTo(final List<?> list, @Nullable String skipToCompareCellValue, final PrintCellIfErrorStrategy printCellIfErrorStrategy) {
        assertSameSize(list);
        assertEqualsTo(printCellIfErrorStrategy, row -> list.get(row.index),
                cell -> (skipToCompareCellValue == null || !skipToCompareCellValue.equals(cell.getValue())));
    }

    private void assertEqualsTo(final PrintCellIfErrorStrategy printCellIfErrorStrategy,
            Function<Row, Object> rowToObjectMapper, Predicate<Entry<String, String>> filterCell) {
        int rowIndex = 0;
        Builder<Integer, String, Optional<?>> actualErrorResultsBuilder = ImmutableTable.builder();
        for (Map<String, String> rowValues : table.getRows()) {
            Object object = rowToObjectMapper.apply(new Row(rowIndex, rowValues));
            for (Entry<String, String> entry : rowValues.entrySet()) {
                if (filterCell.test(entry)) {
                    Object value = getProperty(object, entry.getKey());
                    String cell = entry.getValue();
                    if (!equals(value, cell)) {
                        actualErrorResultsBuilder.put(rowIndex, entry.getKey(), Optional.fromNullable(value));
                    }
                }
            }
            rowIndex++;
        }
        final ImmutableTable<Integer, String, Optional<?>> actualErrorResults = actualErrorResultsBuilder.build();
        if (!actualErrorResults.isEmpty()) {
            JBehaveTableCustomizableOutput output = new JBehaveTableCustomizableOutput(table) {
                @Override
                protected String transformCell(int rowIndex, String cellValue, String header) {
                    if (actualErrorResults.row(rowIndex).containsKey(header)) {
                        Object actualValue = actualErrorResults.row(rowIndex).get(header).orNull();
                        switch (printCellIfErrorStrategy) {
                            case EXPECTED_AND_ACTUAL:
                                return cellValue + " (" + JBehaveFormat.format(actualValue) + ")";
                            case ACTUAL:
                                return JBehaveFormat.format(actualValue);
                            default:
                                throw new RuntimeException();
                        }
                    } else {
                        return cellValue;
                    }
                }

                @Override
                protected void modifyRow(int rowIndex, StringBuilder rowOutput) {
                    int differences = actualErrorResults.row(rowIndex).size();
                    if (differences > 0) {
                        rowOutput.append(" <<< ").append(differences).append(" difference(s)");
                    }
                }
            };

            logger.error("\n" + output.toString());
        }
        Assert.assertTrue(actualErrorResults.isEmpty(), "Has " + actualErrorResults.size() + " difference(s)");
    }

    private Object getProperty(Object bean, String key) {
        try {
            if ("<this>".equals(key)) {
                return bean;
            } else {
                return propertyUtilsBean.getProperty(bean, key);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean equals(Object value, String cell) {
        if (value == null) {
            return JBehaveParser.isNull(cell);
        } else {
            if (JBehaveParser.isNull(cell)) {
                return false;
            } else if (BigDecimal.class.equals(value.getClass()) && cell.startsWith(MORE_OR_LESS_PREFIX)) {
                BigDecimal cellValue = (BigDecimal) parseCell(cell.substring(1), BigDecimal.class);
                return Numbers.equalsMoreOrLess((BigDecimal) value, cellValue);
            } else {
                return value.equals(parseCell(cell, value.getClass()));
            }
        }
    }

    private Object parseCell(String cellValue, Type type) {
        return parameterConverters.convert(cellValue, type);
    }

    private class Row {
        private final int index;
        private final Map<String, String> values;

        Row(int index, Map<String, String> values) {
            this.index = index;
            this.values = values;
        }
    }
}
