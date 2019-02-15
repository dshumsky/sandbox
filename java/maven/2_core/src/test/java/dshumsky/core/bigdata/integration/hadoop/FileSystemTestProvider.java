package dshumsky.core.bigdata.integration.hadoop;


import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

import com.google.inject.Provider;

/**
 * @author Dmitry Shumski (dshumsky@gmail.com)
 */
public class FileSystemTestProvider implements Provider<FileSystem> {

    private final Configuration configuration;

    public FileSystemTestProvider() {
        configuration = new Configuration();
        configuration.set("dfs.replication", "1");
    }

    @Override
    public FileSystem get() {
        try {
            System.setProperty("hadoop.home.dir", "/opt/soft/hadoop");
            URI uri = new URI("hdfs://localhost:9000");
            return FileSystem.get(uri, configuration);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
