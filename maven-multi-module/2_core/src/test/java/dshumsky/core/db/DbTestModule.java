package dshumsky.core.db;

import javax.sql.DataSource;

import com.google.inject.AbstractModule;

import com.google.inject.Inject;
import com.google.inject.persist.PersistService;
import dshumsky.core.db.MyDataSource.Type;
import dshumsky.core.reactjsexample.impl.ReactjsExampleModule;

/**
 * @author dshumski (Dmitry.Shumski@mgm-tp.com)
 */
public class DbTestModule extends AbstractModule {
    @Override
    protected void configure() {
        MyDataSource testDataSource = new MyDataSource(Type.TEST);
        bind(DataSource.class).toInstance(testDataSource);
        install(new ReactjsExampleModule(testDataSource));
        bind(JpaInitializer.class).asEagerSingleton();
    }

    static class JpaInitializer {
        @Inject
        public JpaInitializer(final PersistService persistService) {
            persistService.start();
        }
    }
}
