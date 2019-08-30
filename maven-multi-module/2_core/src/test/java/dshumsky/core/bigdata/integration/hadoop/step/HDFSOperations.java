package dshumsky.core.bigdata.integration.hadoop.step;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.Progressable;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.testng.Assert;

import com.google.inject.Inject;
import dshumsky.core.bigdata.hadoop.FileSystemPathAction;
import dshumsky.core.bigdata.hadoop.HadoopFile;
import dshumsky.core.bigdata.hadoop.HadoopFileFactory;
import dshumsky.core.tests.jbehave.JBehaveMultiLineText;

import static org.testng.Assert.assertTrue;

/**
 * @author Dmitry Shumski (dshumsky@gmail.com)
 */
public class HDFSOperations {

    @Inject
    private HadoopFileFactory fileFactory;

    public HDFSOperations() {
    }

    private final Progressable progressable = new Progressable() {
        @Override
        public void progress() {

        }
    };

    @When("HDFS: clean folder $folder")
    public void cleanFolder(String folder) throws IOException, InterruptedException {
        HadoopFile checkpointDirectory = fileFactory.create(folder);
        checkpointDirectory.delete(true);
        checkpointDirectory.mkdirs();
    }

    @When("HDFS: $src renameTo $dst")
    public void renameTo(String src, String dst) throws IOException, InterruptedException {
        HadoopFile file = fileFactory.create(src);
        assertTrue(file.isFile());
        file.renameTo(dst);
    }

    @When("HDFS: $src copyTo $dst")
    public void copyTo(String src, String dst) throws IOException, InterruptedException {
        HadoopFile file = fileFactory.create(src);
        assertTrue(file.isFile());
        file.copyTo(dst, false, true);
    }

    @When("HDFS: append text to $path (overwrite=$overwrite) $table")
    public void appendTextToFile(String path, final boolean overwrite, ExamplesTable table) throws IOException {
        final JBehaveMultiLineText text = new JBehaveMultiLineText(table);
        HadoopFile file = fileFactory.create(path);
        if (overwrite && file.isFile()) {
            file.delete(false);
        }
        file.execute(new FileSystemPathAction<Void>() {
            @Override
            public Void execute(FileSystem fileSystem, Path path) throws IOException {
                try (FSDataOutputStream outputStream = fileSystem.isFile(path) ? fileSystem.append(path, 4096, progressable) : fileSystem.create(path)) {
                    for (String line : text) {
                        outputStream.write(line.getBytes(StandardCharsets.UTF_8));
                        outputStream.write("\n".getBytes(StandardCharsets.UTF_8));
                    }
                }
                return null;
            }
        });
    }

    @Then("HDFS: content of file $path is $table")
    public void contentOfFileIs(String path, ExamplesTable table) throws IOException {
        JBehaveMultiLineText text = new JBehaveMultiLineText(table);
        HadoopFile file = fileFactory.create(path);
        file.execute(new FileSystemPathAction<Void>() {
            @Override
            public Void execute(FileSystem fileSystem, Path path) throws IOException {
                try (FSDataInputStream inputStream = fileSystem.open(path)) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                    for (String expectedLine : text) {
                        String actualLine = reader.readLine();
                        Assert.assertEquals(actualLine, expectedLine);
                    }
                    Assert.assertNull(reader.readLine(), "File has more lines");
                }
                return null;
            }
        });
    }
}
