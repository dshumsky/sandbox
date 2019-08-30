package dshumsky.core.bigdata.hadoop;

import java.io.IOException;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.assistedinject.Assisted;

/**
 * @author Dmitry Shumski (dshumsky@gmail.com)
 */
public class HadoopFile {
    private final Provider<FileSystem> fileSystemProvider;
    private final String path;

    @Inject
    public HadoopFile(Provider<FileSystem> fileSystemProvider, @Assisted String path) {
        this.fileSystemProvider = fileSystemProvider;
        this.path = path;
    }

    public <R> R execute(FileSystemPathAction<R> action) throws IOException {
        return new FileSystemTemplate(fileSystemProvider).execute(
                fileSystem -> action.execute(fileSystem, getPath(fileSystem)));
    }

    public boolean exists() throws IOException {
        return execute(FileSystem::exists);
    }

    public boolean isDirectory() throws IOException {
        return execute(FileSystem::isDirectory);
    }

    public boolean mkdirs() throws IOException {
        return execute(FileSystem::mkdirs);
    }

    public boolean isFile() throws IOException {
        return execute(FileSystem::isFile);
    }

    public boolean delete(final boolean recursive) throws IOException {
        return execute((fileSystem, path) -> fileSystem.delete(path, recursive));
    }

    public String getPath() throws IOException {
        return execute((fileSystem, path) -> fileSystem.getUri().toString() + this.path);
    }

    private Path getPath(FileSystem fs) {
        return getPath(fs, path);
    }

    public void renameTo(String dst) throws IOException {
        execute((fs, path) -> fs.rename(path, getPath(fs, dst)));
    }

    private static Path getPath(FileSystem fs, String dst) {
        return new Path(new Path(fs.getUri()), dst);
    }

    public void copyTo(String dst, boolean deleteSource, boolean overwrite) throws IOException {
        execute((fs, path) -> {
            FileUtil.copy(fs, path, fs, getPath(fs, dst), deleteSource, overwrite, fs.getConf());
            return null;
        });
    }
}
