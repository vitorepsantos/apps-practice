package br.code.core;

import br.code.pojo.Account;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

public class AccountKeyParserDefaultTest {

    @Test
    public void givenAccountKey_whenParse_thenReturnAccount() {

        String accountKey = "001-1234-0";

        AccountKeyParserDefault accountKeyParserDefault = new AccountKeyParserDefault();
        Account account = accountKeyParserDefault.accountKeyToAccount(accountKey);

        assertNotNull(account);
        assertEquals("001", account.getBranch());
        assertEquals("1234", account.getNumber());
        assertEquals("0", account.getDigit());
    }

}
