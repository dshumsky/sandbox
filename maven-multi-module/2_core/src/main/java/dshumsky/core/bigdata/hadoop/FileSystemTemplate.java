package dshumsky.core.bigdata.hadoop;

import java.io.IOException;

import org.apache.hadoop.fs.FileSystem;

import com.google.inject.Provider;

/**
 * @author Dmitry Shumski (dshumsky@gmail.com)
 */
public class FileSystemTemplate {
    private final Provider<FileSystem> provider;

    public FileSystemTemplate(Provider<FileSystem> provider) {
        this.provider = provider;
    }

    public <R, E extends Exception> R execute(FileSystemAction<R, E> action) throws E, IOException {
        try (FileSystem fileSystem = provider.get()) {
            return action.execute(fileSystem);
        }
    }
}
