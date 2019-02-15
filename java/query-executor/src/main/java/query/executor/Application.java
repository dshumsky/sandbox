package query.executor;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootApplication
@EnableTransactionManagement
public class Application {

    interface Opts {
        String QUERY = "query/executor";
        String FILE = "file";
        String N = "n";
        String FETCH_SIZE = "fetchSize";
    }

    @Autowired
    QueryExecutor queryExecutor;

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(final ApplicationContext ctx) {
        return new CommandLineRunner() {
            @Override
            public void run(final String... args) throws Exception {

                final Options options = new Options();

                Option o = new Option("q", Opts.QUERY, true, "query to execute");
                o.setRequired(false);
                options.addOption(o);

                o = new Option("f", Opts.FILE, true, "file with query to execute");
                o.setRequired(false);
                options.addOption(o);

                o = new Option("n", Opts.N, true, "output every N-th row (default: 1000)");
                o.setRequired(false);
                o.setType(Number.class);
                options.addOption(o);

                o = new Option("s", Opts.FETCH_SIZE, true, "fetchSize (default: 10)");
                o.setRequired(false);
                o.setType(Number.class);
                options.addOption(o);

                final CommandLineParser parser = new DefaultParser();
                final HelpFormatter formatter = new HelpFormatter();
                final CommandLine cmd;

                try {
                    cmd = parser.parse(options, args);
                    String query = cmd.getOptionValue(Opts.QUERY);
                    final String fileName = cmd.getOptionValue(Opts.FILE);
                    if (query != null) {
                        // fine
                    } else if (fileName != null) {
                        final byte[] bytes = Files.readAllBytes(Paths.get(fileName));
                        query = new String(bytes);
                    } else {
                        throw new ParseException("Query is undefined");
                    }
                    final Number n = (Number) cmd.getParsedOptionValue(Opts.N);
                    final Number fetchSize = (Number) cmd.getParsedOptionValue(Opts.FETCH_SIZE);
                    queryExecutor.execute(query,
                        n != null ? n.intValue() : 100,
                        fetchSize != null ? fetchSize.intValue() : 10);
                } catch (final ParseException e) {
                    System.out.println(e.getMessage());
                    formatter.printHelp("Query Executor", options);
                    System.exit(1);
                }
            }
        };
    }
}
