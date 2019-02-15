package dshumsky.core.bigdata.hadoop;

import org.apache.hadoop.fs.FileSystem;

import com.google.inject.Provider;
import com.google.inject.assistedinject.Assisted;

/**
 * @author Dmitry Shumski (dshumsky@gmail.com)
 */
public class HadoopTextFile extends HadoopFile {

    public HadoopTextFile(Provider<FileSystem> fileSystemProvider, @Assisted String path) {
        super(fileSystemProvider, path);
    }
}
