package br.code.servlet;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import java.util.HashMap;
import java.util.Map;

public class GuiceConfig extends GuiceServletContextListener {

    private AbstractModule module;

    public GuiceConfig() {
        this.module = new RESTModule();
    }

    public GuiceConfig(AbstractModule module) {
        this.module = module;
    }

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new JerseyServletModule() {
            @Override
            protected void configureServlets() {
                install(module);
                Map<String, String> params = new HashMap<String, String>();
                params.put("com.sun.jersey.config.property.packages", "br.code.resources");
                params.put("com.sun.jersey.api.json.POJOMappingFeature", "true");
                serve("/*").with(GuiceContainer.class, params);
            }
        });
    }

}
