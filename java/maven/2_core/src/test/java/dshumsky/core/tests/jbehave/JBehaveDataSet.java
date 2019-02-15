package dshumsky.core.tests.jbehave;

import org.dbunit.dataset.CachedDataSet;
import org.dbunit.dataset.DataSetException;
import org.jbehave.core.model.ExamplesTable;

/**
 * @author dshumski (Dmitry.Shumski@mgm-tp.com)
 */
public class JBehaveDataSet extends CachedDataSet {

    public JBehaveDataSet(String tableName, ExamplesTable examplesTable) throws DataSetException {
        super(new JBehaveDataSetProducer(tableName, examplesTable));
    }
}
