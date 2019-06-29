package br.code.core;

import br.code.dao.AccountDAO;
import br.code.pojo.Account;
import br.code.pojo.AccountBalance;
import br.code.pojo.AccountDepositTransaction;
import br.code.pojo.AccountTransferTransaction;
import com.google.inject.Inject;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountControllerDefault implements AccountController {

    private AccountDAO accountDAO;

    private AccountKeyParser accountKeyParser;

    @Inject
    public void setAccountDAO(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    @Inject
    public void setAccountKeyParser(AccountKeyParser accountKeyParser) {
        this.accountKeyParser = accountKeyParser;
    }

    @Override
    public void deposit(AccountDepositTransaction accountDepositTransaction) {
        String accountKey = accountDepositTransaction.getAccount().toString();
        BigDecimal amount = accountDepositTransaction.getAmount();
        accountDAO.deposit(accountKey, amount);
    }

    @Override
    public void transfer(AccountTransferTransaction accountTransferTransaction) {

        String originAccountKey = accountTransferTransaction.getAccountOrigin().toString();
        String destinationAccountKey = accountTransferTransaction.getAccountDestination().toString();

        if (StringUtils.equals(originAccountKey, destinationAccountKey)) {
            throw new RuntimeException("Illegal arguments.");
        }

        BigDecimal amount = accountTransferTransaction.getAmount();

        accountDAO.withdraw(originAccountKey, amount);
        accountDAO.deposit(destinationAccountKey, amount);
    }

    @Override
    public  List<AccountBalance> balances() {
        Set<Map.Entry<String, BigDecimal>> balances = accountDAO.balances();
        return balances.stream().map(entry -> {
            Account account = accountKeyParser.accountKeyToAccount(entry.getKey());
            BigDecimal amount = entry.getValue();
           return new AccountBalance(account, amount);
        }).collect(Collectors.toList());
    }

}
