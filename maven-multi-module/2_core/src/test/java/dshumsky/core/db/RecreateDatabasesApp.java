package dshumsky.core.db;

import java.net.URISyntaxException;

import org.flywaydb.core.Flyway;

import dshumsky.core.db.MyDataSource.Type;

/**
 * @author dshumski (Dmitry.Shumski@mgm-tp.com)
 */
public class RecreateDatabasesApp {
    public static void main(String[] args) throws URISyntaxException {

        Flyway flyway = new Flyway();

        // test
        flyway.setDataSource(new MyDataSource(Type.TEST));
        flyway.setLocations("db/schema");
        flyway.clean();
        flyway.migrate();

        // example
        flyway.setDataSource(new MyDataSource(Type.EXAMPLE));
        flyway.setLocations("db/schema");
        flyway.clean();
        flyway.migrate();

        flyway.setLocations("db/data");
        flyway.migrate();
    }
}
