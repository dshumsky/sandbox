package dshumsky.core.bigdata.hadoop;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * @author Dmitry Shumski (dshumsky@gmail.com)
 */
public abstract class HadoopModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HadoopFile.class, HadoopFile.class)
                .build(HadoopFileFactory.class));
        bindFileSystem();
    }

    public abstract void bindFileSystem();
}
