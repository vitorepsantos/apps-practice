package br.code.servlet;

import br.code.core.AccountController;
import br.code.core.AccountControllerDefault;
import br.code.core.AccountKeyParser;
import br.code.core.AccountKeyParserDefault;
import br.code.dao.Storage;
import br.code.dao.StorageDefault;
import com.google.inject.AbstractModule;

public class RESTModule extends AbstractModule {

    @Override
    public void configure() {
        bind(AccountController.class).to(AccountControllerDefault.class).asEagerSingleton();
        bind(Storage.class).to(StorageDefault.class).asEagerSingleton();
        bind(AccountKeyParser.class).to(AccountKeyParserDefault.class).asEagerSingleton();
    }

}
