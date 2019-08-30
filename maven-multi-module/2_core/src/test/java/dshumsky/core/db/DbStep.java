package dshumsky.core.db;

import java.util.ArrayList;

import javax.sql.DataSource;

import org.dbunit.DBTestCase;
import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.IDatabaseTester;
import org.dbunit.dataset.CompositeDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.model.ExamplesTable;

import com.google.inject.Inject;
import dshumsky.core.tests.jbehave.JBehaveDataSet;

/**
 * @author dshumski (Dmitry.Shumski@mgm-tp.com)
 */
public class DbStep {

    private final ArrayList<IDataSet> dataSets = new ArrayList<>();
    private final DBTestCaseExtension dbTestCase;
    private final DataSource dataSource;

    @Inject
    public DbStep(DataSource dataSource) {
        this.dataSource = dataSource;
        this.dbTestCase = new DBTestCaseExtension(dataSource);
    }

    @Given("DB: Table ${tableName}: ${table}")
    public void table(String tableName, ExamplesTable table) throws DataSetException {
        IDataSet dataSet = new JBehaveDataSet(tableName.trim(), table);
        dataSets.add(dataSet);
    }

    @Given("DB: setUp")
    public void setUp() throws Exception {
        dbTestCase.setUp();
    }

    @Given("DB: tearDown")
    public void tearDown() throws Exception {
        dbTestCase.tearDown();
    }

    class DBTestCaseExtension extends DBTestCase {
        private final DataSource dataSource;

        public DBTestCaseExtension(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        @Override
        protected IDataSet getDataSet() throws Exception {
            return new CompositeDataSet(dataSets.toArray(new IDataSet[dataSets.size()]));
        }

        @Override
        public void setUp() throws Exception {
            super.setUp();
        }

        @Override
        public void tearDown() throws Exception {
            super.tearDown();
        }

        @Override
        protected DatabaseOperation getTearDownOperation() throws Exception {
            return DatabaseOperation.DELETE_ALL;
        }

        @Override
        protected IDatabaseTester newDatabaseTester() throws Exception {
            return new DataSourceDatabaseTester(dataSource);
        }
    };
}
