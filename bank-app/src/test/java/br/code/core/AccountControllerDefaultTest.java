package br.code.core;

import br.code.dao.AccountDAO;
import br.code.pojo.Account;
import br.code.pojo.AccountBalance;
import br.code.pojo.AccountDepositTransaction;
import br.code.pojo.AccountTransferTransaction;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AccountControllerDefaultTest {

    @Test
    public void givenAccountDepositTransaction_whenDeposit_thenCallAccountDao() {

        AccountDAO accountDAO = mock(AccountDAO.class);

        Account account = new Account("001", "1256", "0");
        BigDecimal amount = new BigDecimal(100L);
        AccountDepositTransaction accountDepositTransaction = new AccountDepositTransaction(account, amount);

        AccountControllerDefault accountController = new AccountControllerDefault();
        accountController.setAccountDAO(accountDAO);

        accountController.deposit(accountDepositTransaction);

        int timesExpected = 1;

        verify(accountDAO, times(timesExpected)).deposit(account.toString(), amount);
    }

    @Test
    public void givenAccountTransferTransaction_whenSameAccounts_thenThrowsException() {

        Account accountOrigin = new Account("001", "1256", "0");
        Account accountDestination = new Account("001", "1256", "0");

        BigDecimal amount = new BigDecimal(100L);

        AccountTransferTransaction accountTransferTransaction = new AccountTransferTransaction(accountOrigin, accountDestination, amount);

        AccountController accountController = new AccountControllerDefault();

        Exception exception = null;

        try {
            accountController.transfer(accountTransferTransaction);
        } catch (Exception e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(RuntimeException.class, exception.getClass());
        assertEquals("Illegal arguments.", exception.getMessage());
    }

    @Test
    public void givenAccountTransferTransaction_whenTransfer_thenCallWithdrawAndDeposit() {

        AccountDAO accountDAO = mock(AccountDAO.class);

        Account accountOrigin = new Account("001", "1256", "0");
        Account accountDestination = new Account("001", "1257", "0");

        BigDecimal amount = new BigDecimal(100L);

        AccountTransferTransaction accountTransferTransaction = new AccountTransferTransaction(accountOrigin, accountDestination, amount);

        AccountControllerDefault accountController = new AccountControllerDefault();
        accountController.setAccountDAO(accountDAO);

        accountController.transfer(accountTransferTransaction);

        int timesExpected = 1;

        verify(accountDAO, times(timesExpected)).withdraw(accountOrigin.toString(), amount);
        verify(accountDAO, times(timesExpected)).deposit(accountDestination.toString(), amount);
    }

    @Test
    public void whenGetBalances_thenReturnAccountBalances() {

        AccountDAO accountDAO = mock(AccountDAO.class);

        AccountKeyParser accountKeyParser = mock(AccountKeyParser.class);

        String accountKey = "001-1234-0";
        BigDecimal amount = new BigDecimal(100L);
        Account account = new Account("001", "1234", "0");

        Map.Entry<String, BigDecimal> entry = new AbstractMap.SimpleEntry<>(accountKey, amount);
        Set<Map.Entry<String, BigDecimal>> setEntries = new HashSet<>();
        setEntries.add(entry);

        when(accountDAO.balances()).thenReturn(setEntries);
        when(accountKeyParser.accountKeyToAccount(accountKey)).thenReturn(account);

        AccountControllerDefault accountController = new AccountControllerDefault();
        accountController.setAccountDAO(accountDAO);
        accountController.setAccountKeyParser(accountKeyParser);

        List<AccountBalance> balances = accountController.balances();

        assertNotNull(balances);
        assertEquals(1, balances.size());

        AccountBalance accountBalance = balances.get(0);

        assertEquals("001", accountBalance.getAccount().getBranch());
        assertEquals("1234", accountBalance.getAccount().getNumber());
        assertEquals("0", accountBalance.getAccount().getDigit());
        assertEquals(amount, accountBalance.getBalance());
    }

}
