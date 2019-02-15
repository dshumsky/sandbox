package dshumsky.core.reactjsexample.impl;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.cfg.Environment;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.persist.jpa.JpaPersistModule;
import dshumsky.core.reactjsexample.api.TripDao;
import dshumsky.core.reactjsexample.api.UserDao;

/**
 * @author dshumski (Dmitry.Shumski@mgm-tp.com)
 */
public class ReactjsExampleModule extends AbstractModule {
    private final DataSource dataSource;

    public ReactjsExampleModule(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    protected void configure() {
        // guice-persist
        Map<String, Object> properties = new HashMap<>();
        properties.put(Environment.DATASOURCE, dataSource);
        install(new JpaPersistModule("ReactjsExample").properties(properties));

        bind(UserDao.class).to(UserDaoImpl.class).in(Scopes.SINGLETON);
        bind(TripDao.class).to(TripDaoImpl.class).in(Scopes.SINGLETON);
    }
}
