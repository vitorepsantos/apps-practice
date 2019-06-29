package br.code.utils;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class GuiceJUnitRunner extends BlockJUnit4ClassRunner {

    private Injector injector;

    @Override
    public Object createTest() throws Exception {
        Object obj = super.createTest();
        injector.injectMembers(obj);
        return obj;
    }

    public GuiceJUnitRunner(Class<?> klass) throws InitializationError {
        super(klass);
        injector = Guice.createInjector(new ModuleIT());
    }

}