package dshumsky.core.bigdata.hadoop;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * @author Dmitry Shumski (dshumsky@gmail.com)
 */
public class MultiLineHadoopFile {

    private final FileSystem fs;
    private final Path path;

    public MultiLineHadoopFile(FileSystem fs, Path path) {
        this.fs = fs;
        this.path = path;
    }

    public void append(Iterable<String> lines) throws IOException {
        try(OutputStream outputStream = fs.create(path)) {
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            for (String line : lines) {
                bufferedWriter.write(line);
            }
        }
    }
}
