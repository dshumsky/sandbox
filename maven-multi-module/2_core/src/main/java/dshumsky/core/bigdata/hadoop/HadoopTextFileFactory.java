package dshumsky.core.bigdata.hadoop;

/**
 * @author Dmitry Shumski (dshumsky@gmail.com)
 */
public interface HadoopTextFileFactory {
    HadoopTextFile create(String path);
}
