package dshumsky.core.bigdata.hadoop;

import java.io.IOException;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * @author Dmitry Shumski (dshumsky@gmail.com)
 */
public interface FileSystemPathAction<R> {
    R execute(FileSystem fileSystem, Path path) throws IOException;
}
