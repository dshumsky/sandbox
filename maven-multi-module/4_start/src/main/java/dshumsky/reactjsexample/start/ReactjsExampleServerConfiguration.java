package dshumsky.reactjsexample.start;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import dshumsky.core.reactjsexample.api.TripDao;
import dshumsky.core.reactjsexample.api.UserDao;
import dshumsky.core.reactjsexample.impl.ReactjsExampleModule;

/**
 * @author dshumski (Dmitry.Shumski@mgm-tp.com)
 */
@Configuration
public class ReactjsExampleServerConfiguration {

    private final Injector injector;

    public ReactjsExampleServerConfiguration(@Autowired DataSource dataSource) {
        injector = Guice.createInjector(new ReactjsExampleModule(dataSource));
        injector.getInstance(PersistService.class).start();
    }

    @Bean
    public UserDao userDao() {
        return injector.getInstance(UserDao.class);
    }

    @Bean
    public TripDao tripDao() {
        return injector.getInstance(TripDao.class);
    }
}
