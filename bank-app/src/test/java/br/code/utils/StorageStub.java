package br.code.utils;

import br.code.dao.Storage;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StorageStub implements Storage {

    private static Map<String, BigDecimal> storage = new ConcurrentHashMap<>();

    @Override
    public Map<String, BigDecimal> getStorage() {
        return storage;
    }

    @Override
    public void restore() {
        storage = new ConcurrentHashMap<>();
    }
}