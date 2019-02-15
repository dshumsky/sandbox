package dshumsky.core.db;

import org.apache.commons.dbcp.BasicDataSource;

import com.p6spy.engine.spy.P6SpyDriver;

/**
 * @author dshumski (Dmitry.Shumski@mgm-tp.com)
 */
public class MyDataSource extends BasicDataSource {

    public enum Type {
        TEST("jdbc:p6spy:h2:/opt/p/sandbox/2_core/data/h2-test.db;AUTO_SERVER=true"),
        EXAMPLE("jdbc:p6spy:h2:/opt/p/sandbox/2_core/data/h2.db;AUTO_SERVER=true");

        private final String url;

        Type(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }

    public MyDataSource(Type type) {
        super();
        setDriverClassName(P6SpyDriver.class.getName());
        setUrl(type.getUrl());
        setUsername("TP");
        setPassword("TP");
    }
}
