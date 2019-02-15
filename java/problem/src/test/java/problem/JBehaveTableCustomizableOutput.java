package problem;

import org.apache.commons.lang.StringUtils;
import org.jbehave.core.model.ExamplesTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Provides customizable pretty-print output for JBehave-table.
 * E.x. <br/>
 * | column#1 | column#2 | <br/>
 * | value-1(**) | value-2 | <<< some customizable comment for row <br/>
 * | value-3 | value-4 | <br/>
 */
public class JBehaveTableCustomizableOutput {
    public static final String ROW_START = "| ";
    public static final String ROW_SEPARATOR = " | ";
    public static final String ROW_END = " |";

    protected final ExamplesTable table;

    /**
     * Constructor
     *
     * @param table - the JBehave table
     */
    public JBehaveTableCustomizableOutput(ExamplesTable table) {
        this.table = table;
    }

    /**
     * @return formatted table
     */
    public String toString() {
        ArrayList<List<String>> rows = new ArrayList<>(table.getRowCount() + 1);
        int columnCount = table.getHeaders().size();
        int[] columnWidth = new int[columnCount];
        Arrays.fill(columnWidth, 0);
        ArrayList<String> headerRow = new ArrayList<>(columnCount);
        rows.add(headerRow);
        int columnIndex = 0;
        for (String header : table.getHeaders()) {
            headerRow.add(header);
            columnWidth[columnIndex] = Math.max(columnWidth[columnIndex], header.length());
            columnIndex++;
        }
        int rowIndex = 0;
        for (Map<String, String> tableRow : table.getRows()) {
            ArrayList<String> row = new ArrayList<>(columnCount);
            columnIndex = 0;
            for (String header : table.getHeaders()) {
                String modifiedCell = transformCell(rowIndex, tableRow.get(header), header);
                row.add(modifiedCell);
                columnWidth[columnIndex] = Math.max(columnWidth[columnIndex], modifiedCell.length());
                columnIndex++;
            }
            rowIndex++;
            rows.add(row);
        }
        StringBuilder tableOutput = new StringBuilder();
        rowIndex = -1;
        StringBuilder rowOutput = new StringBuilder();
        for (List<String> row : rows) {
            rowOutput.setLength(0);
            rowOutput.append(ROW_START);
            columnIndex = 0;
            for (String cell : row) {
                rowOutput.append(StringUtils.rightPad(cell, columnWidth[columnIndex])).append(ROW_SEPARATOR);
                columnIndex++;
            }
            rowOutput.setLength(rowOutput.length() - 3);
            rowOutput.append(ROW_END);
            modifyRow(rowIndex, rowOutput);
            rowOutput.append("\n");
            tableOutput.append(rowOutput.toString());
            rowIndex++;
        }
        return tableOutput.toString();
    }

    /**
     * Can be overridden to change cell-value
     *
     * @param rowIndex - the row index
     * @param cellValue - the original cell value
     * @param header - the header of column
     * @return changed cell-value
     */
    protected String transformCell(int rowIndex, String cellValue, String header) {
        return cellValue;
    }

    /**
     * Can be overridden to change row-output
     *
     * @param rowIndex - the row index or -1 if it's header-row
     * @param rowOutput - the original row-output
     */
    protected void modifyRow(int rowIndex, StringBuilder rowOutput) {
        // do nothing
    }
}
