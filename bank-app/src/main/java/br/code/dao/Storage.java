package br.code.dao;

import java.math.BigDecimal;
import java.util.Map;

public interface Storage {

    Map<String, BigDecimal> getStorage();

    void restore();

}
