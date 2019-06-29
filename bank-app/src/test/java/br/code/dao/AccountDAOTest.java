package br.code.dao;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AccountDAOTest {

    @Test
    public void givenDeposit_WhenAccountNotExist_thenCreateAndDeposit() {

        Map<String, BigDecimal> map = new HashMap<>();
        Storage storage = mock(Storage.class);

        when(storage.getStorage()).thenReturn(map);

        String accountKey = "001-2345-0";
        BigDecimal amount = new BigDecimal(10L);

        AccountDAO accountDAO = new AccountDAO(storage);

        accountDAO.deposit(accountKey, amount);

        assertTrue(map.containsKey(accountKey));
        assertEquals(amount, map.get(accountKey));
    }

    @Test
    public void givenDeposit_WhenAccountExist_thenAddDeposit() {

        Map<String, BigDecimal> map = new HashMap<>();
        Storage storage = mock(Storage.class);

        when(storage.getStorage()).thenReturn(map);

        String accountKey = "001-2345-0";
        BigDecimal amount = new BigDecimal(10L);

        map.put(accountKey, amount);

        AccountDAO accountDAO = new AccountDAO(storage);

        accountDAO.deposit(accountKey, amount);

        BigDecimal expectedAmount = amount.add(amount);

        assertTrue(map.containsKey(accountKey));
        assertEquals(expectedAmount, map.get(accountKey));
    }

    @Test
    public void givenWithdraw_WhenAccountNotExist_thenThrowsException() {

        Map<String, BigDecimal> map = new HashMap<>();
        Storage storage = mock(Storage.class);

        when(storage.getStorage()).thenReturn(map);

        String accountKey = "001-2345-0";
        BigDecimal amount = new BigDecimal(10L);

        AccountDAO accountDAO = new AccountDAO(storage);

        Exception exception = null;

        try {
            accountDAO.withdraw(accountKey, amount);
        } catch (Exception e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(RuntimeException.class, exception.getClass());
        assertEquals("Account not found.", exception.getMessage());
    }

    @Test
    public void givenWithdraw_WhenAccountExistAndHasNotFunds_thenThrowsException() {

        Map<String, BigDecimal> map = new HashMap<>();
        Storage storage = mock(Storage.class);

        when(storage.getStorage()).thenReturn(map);

        String accountKey = "001-2345-0";
        BigDecimal amount = new BigDecimal(100L);
        BigDecimal currentBalance = new BigDecimal(10L);

        map.put(accountKey, currentBalance);

        AccountDAO accountDAO = new AccountDAO(storage);

        Exception exception = null;

        try {
            accountDAO.withdraw(accountKey, amount);
        } catch (Exception e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(RuntimeException.class, exception.getClass());
        assertEquals("Not enough funds.", exception.getMessage());
    }

    @Test
    public void givenWithdraw_WhenAccountExistAndHasFunds_thenSubtractFromBalance() {

        Map<String, BigDecimal> map = new HashMap<>();
        Storage storage = mock(Storage.class);

        when(storage.getStorage()).thenReturn(map);

        String accountKey = "001-2345-0";
        BigDecimal amount = new BigDecimal(100L);
        BigDecimal withdrawAmount = new BigDecimal(10L);

        map.put(accountKey, amount);

        AccountDAO accountDAO = new AccountDAO(storage);

        accountDAO.withdraw(accountKey, withdrawAmount);

        BigDecimal expectedAmount = amount.subtract(withdrawAmount);

        assertTrue(map.containsKey(accountKey));
        assertEquals(expectedAmount, map.get(accountKey));
    }

    @Test
    public void whenCallBalances_thenReturnAccountKeysAndBalance() {
        Map<String, BigDecimal> map = new HashMap<>();
        Storage storage = mock(Storage.class);

        when(storage.getStorage()).thenReturn(map);

        String accountKey = "001-2345-0";
        BigDecimal amount = new BigDecimal(100L);

        map.put(accountKey, amount);

        AccountDAO accountDAO = new AccountDAO(storage);

        Set<Map.Entry<String, BigDecimal>> balances = accountDAO.balances();

        assertNotNull(balances);
        assertEquals(1, balances.size());

        Map.Entry<String, BigDecimal> accountBalance = balances.iterator().next();

        assertEquals(accountKey, accountBalance.getKey());
        assertEquals(amount, accountBalance.getValue());
    }

}
