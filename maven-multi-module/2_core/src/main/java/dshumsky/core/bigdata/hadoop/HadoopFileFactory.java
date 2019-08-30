package dshumsky.core.bigdata.hadoop;

/**
 * @author Dmitry Shumski (dshumsky@gmail.com)
 */
public interface HadoopFileFactory {
    HadoopFile create(String path);
}
