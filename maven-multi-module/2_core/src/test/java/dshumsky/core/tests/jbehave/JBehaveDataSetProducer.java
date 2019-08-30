package dshumsky.core.tests.jbehave;

import java.util.List;
import java.util.Map;

import org.dbunit.dataset.Column;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.DefaultTableMetaData;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.datatype.DataType;
import org.dbunit.dataset.stream.IDataSetConsumer;
import org.dbunit.dataset.stream.IDataSetProducer;
import org.jbehave.core.model.ExamplesTable;

/**
 * @author dshumski (Dmitry.Shumski@mgm-tp.com)
 */
public class JBehaveDataSetProducer implements IDataSetProducer {

    private final String tableName;
    private final ExamplesTable examplesTable;
    private IDataSetConsumer consumer;

    public JBehaveDataSetProducer(String tableName, ExamplesTable examplesTable) {
        this.tableName = tableName;
        this.examplesTable = examplesTable;
    }

    @Override
    public void setConsumer(IDataSetConsumer consumer) throws DataSetException {
        this.consumer = consumer;
    }

    @Override
    public void produce() throws DataSetException {
        consumer.startDataSet();
        List<String> headers = examplesTable.getHeaders();
        consumer.startTable(tableMetaData(headers));

        for (int i = 0; i < examplesTable.getRowCount(); i++) {
            Map<String, String> row = examplesTable.getRow(i);
            consumer.row(getRowObjects(headers, row));
        }

        consumer.endTable();
        consumer.endDataSet();
    }

    private Object[] getRowObjects(List<String> headers, Map<String, String> row) {
        return headers.stream()
                .map(header -> stringToValue(row.get(header)))
                .toArray(Object[]::new);
    }

    private Object stringToValue(String s) {
        if (s == null || "<null>".equals(s) || s.length() == 0) {
            return ITable.NO_VALUE;
        } else {
            return s;
        }
    }

    private DefaultTableMetaData tableMetaData(List<String> headers) {
        Column[] columns = headers.stream()
                .map(header -> new Column(header, DataType.UNKNOWN))
                .toArray(size -> new Column[headers.size()]);
        return new DefaultTableMetaData(tableName, columns);
    }

}
