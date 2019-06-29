package br.code.utils;

import br.code.core.AccountController;
import br.code.core.AccountControllerDefault;
import br.code.core.AccountKeyParser;
import br.code.core.AccountKeyParserDefault;
import br.code.dao.Storage;
import com.google.inject.AbstractModule;

public class ModuleIT extends AbstractModule {

    @Override
    public void configure() {
        bind(AccountController.class).to(AccountControllerDefault.class);
        bind(AccountKeyParser.class).to(AccountKeyParserDefault.class);

        bind(Storage.class).to(StorageStub.class);
    }

}

