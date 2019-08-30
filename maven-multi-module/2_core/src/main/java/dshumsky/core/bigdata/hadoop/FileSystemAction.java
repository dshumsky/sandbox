package dshumsky.core.bigdata.hadoop;

import org.apache.hadoop.fs.FileSystem;

/**
 * @author Dmitry Shumski (dshumsky@gmail.com)
 */
public interface FileSystemAction <R, E extends Exception> {
    R execute(FileSystem fileSystem) throws E;
}
