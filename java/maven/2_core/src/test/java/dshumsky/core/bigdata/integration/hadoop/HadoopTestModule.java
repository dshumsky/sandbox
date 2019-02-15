package dshumsky.core.bigdata.integration.hadoop;

import org.apache.hadoop.fs.FileSystem;

import dshumsky.core.bigdata.hadoop.HadoopModule;

/**
 * @author Dmitry Shumski (dshumsky@gmail.com)
 */
public class HadoopTestModule extends HadoopModule {
    @Override
    public void bindFileSystem() {
        bind(FileSystem.class).toProvider(FileSystemTestProvider.class);
    }
}
