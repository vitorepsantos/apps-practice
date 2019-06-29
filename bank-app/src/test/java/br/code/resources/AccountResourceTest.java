package br.code.resources;

import br.code.core.AccountController;
import br.code.pojo.Account;
import br.code.pojo.AccountBalance;
import br.code.pojo.AccountDepositTransaction;
import br.code.pojo.AccountTransferTransaction;
import br.code.pojo.RequestStatus;
import br.code.pojo.ResponseWrapper;
import org.junit.Test;

import javax.ws.rs.core.Response;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AccountResourceTest {

    @Test
    public void givenAccountDepositTransaction_whenDeposit_thenCallAccountController() {

        AccountController accountController = mock(AccountController.class);
        AccountDepositTransaction accountDepositTransaction = mock(AccountDepositTransaction.class);

        AccountResource accountResource = new AccountResource(accountController);

        Response response = accountResource.deposit(accountDepositTransaction);

        int expectedTimes = 1;

        verify(accountController, times(expectedTimes)).deposit(accountDepositTransaction);
        assertNotNull(response);
        assertEquals(ResponseWrapper.class, response.getEntity().getClass());

        ResponseWrapper r = (ResponseWrapper) response.getEntity();

        assertEquals(RequestStatus.SUCCESS, r.getStatus());
        assertEquals(r.getMsg(), "Operation Completed");
        assertSame(r.getData(), accountDepositTransaction);
    }

    @Test
    public void givenAccountDepositTransaction_whenDepositThrowsException_thenHandleException() {

        AccountController accountController = mock(AccountController.class);
        AccountDepositTransaction accountDepositTransaction = mock(AccountDepositTransaction.class);

        doThrow(new RuntimeException("Failed")).when(accountController).deposit(accountDepositTransaction);

        AccountResource accountResource = new AccountResource(accountController);

        Response response = accountResource.deposit(accountDepositTransaction);

        int expectedTimes = 1;

        verify(accountController, times(expectedTimes)).deposit(accountDepositTransaction);
        assertNotNull(response);
        assertEquals(ResponseWrapper.class, response.getEntity().getClass());

        ResponseWrapper r = (ResponseWrapper) response.getEntity();

        assertEquals(RequestStatus.FAIL, r.getStatus());
        assertEquals(r.getMsg(), "Failed");
        assertSame(r.getData(), accountDepositTransaction);
    }

    @Test
    public void givenAccountTransferTransaction_whenTransfer_thenCallAccountController() {

        AccountController accountController = mock(AccountController.class);
        AccountTransferTransaction accountTransferTransaction = mock(AccountTransferTransaction.class);

        AccountResource accountResource = new AccountResource(accountController);

        Response response = accountResource.transfer(accountTransferTransaction);

        int expectedTimes = 1;

        verify(accountController, times(expectedTimes)).transfer(accountTransferTransaction);
        assertNotNull(response);
        assertEquals(ResponseWrapper.class, response.getEntity().getClass());

        ResponseWrapper r = (ResponseWrapper) response.getEntity();

        assertEquals(RequestStatus.SUCCESS, r.getStatus());
        assertEquals(r.getMsg(), "Operation Completed");
        assertSame(r.getData(), accountTransferTransaction);
    }

    @Test
    public void givenAccountTransferTransaction_whenTransferThrowsException_thenHandleException() {

        AccountController accountController = mock(AccountController.class);
        AccountTransferTransaction accountTransferTransaction = mock(AccountTransferTransaction.class);

        doThrow(new RuntimeException("Failed")).when(accountController).transfer(accountTransferTransaction);

        AccountResource accountResource = new AccountResource(accountController);

        Response response = accountResource.transfer(accountTransferTransaction);

        int expectedTimes = 1;

        verify(accountController, times(expectedTimes)).transfer(accountTransferTransaction);
        assertNotNull(response);
        assertEquals(ResponseWrapper.class, response.getEntity().getClass());

        ResponseWrapper r = (ResponseWrapper) response.getEntity();

        assertEquals(RequestStatus.FAIL, r.getStatus());
        assertEquals(r.getMsg(), "Failed");
        assertSame(r.getData(), accountTransferTransaction);
    }

    @Test
    public void whenCallBalances_thenReturnAvailableAccountBalances() {

        AccountController accountController = mock(AccountController.class);

        Account account = new Account("001", "1234", "0");
        BigDecimal amount = new BigDecimal(100L);
        AccountBalance accountBalance = new AccountBalance(account, amount);
        List<AccountBalance> balances = Arrays.asList(accountBalance);

        when(accountController.balances()).thenReturn(balances);

        AccountResource accountResource = new AccountResource(accountController);

        Response response = accountResource.balances();

        int expectedTimes = 1;

        verify(accountController, times(expectedTimes)).balances();
        assertNotNull(response);
        assertEquals(ResponseWrapper.class, response.getEntity().getClass());

        ResponseWrapper r = (ResponseWrapper) response.getEntity();

        assertEquals(RequestStatus.SUCCESS, r.getStatus());
        assertEquals(r.getMsg(), "Operation Completed");
        assertSame(r.getData(), balances);
    }

    @Test
    public void whenCallBalancesAndThrowsException_thenHandleAndReturnEmpty() {

        AccountController accountController = mock(AccountController.class);

        when(accountController.balances()).thenThrow(new RuntimeException("Failed"));

        AccountResource accountResource = new AccountResource(accountController);

        Response response = accountResource.balances();

        int expectedTimes = 1;

        verify(accountController, times(expectedTimes)).balances();
        assertNotNull(response);
        assertEquals(ResponseWrapper.class, response.getEntity().getClass());

        ResponseWrapper r = (ResponseWrapper) response.getEntity();

        assertEquals(RequestStatus.FAIL, r.getStatus());
        assertEquals("Failed", r.getMsg());
        assertEquals(Collections.emptyList().getClass(), r.getData().getClass());
    }

}
