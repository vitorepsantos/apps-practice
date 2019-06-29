package br.code.dao;

import com.google.inject.Inject;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

public class AccountDAO {

    private Map<String, BigDecimal> storage;

    @Inject
    public AccountDAO(Storage storage) {
        this.storage = storage.getStorage();
    }

    public synchronized void withdraw(String accountKey, BigDecimal amount) {

        if (!storage.containsKey(accountKey)) {
            throw new RuntimeException("Account not found.");
        }

        BigDecimal currentBalance = storage.get(accountKey);

        if (currentBalance.compareTo(amount) >= 0) {
            BigDecimal newBalance = currentBalance.subtract(amount);
            storage.put(accountKey, newBalance);
        } else {
            throw new RuntimeException("Not enough funds.");
        }
    }

    public synchronized void deposit(String accountKey, BigDecimal amount) {

        if (!storage.containsKey(accountKey)) {
            storage.put(accountKey, new BigDecimal(0L));
        }

        BigDecimal currentBalance = storage.get(accountKey);
        BigDecimal newBalance = currentBalance.add(amount);
        storage.put(accountKey, newBalance);
    }

    public Set<Map.Entry<String, BigDecimal>> balances() {
        return storage.entrySet();
    }

}
