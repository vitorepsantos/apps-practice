package br.code.core;

import br.code.pojo.AccountBalance;
import br.code.pojo.AccountDepositTransaction;
import br.code.pojo.AccountTransferTransaction;

import java.util.List;

public interface AccountController {

    void deposit(AccountDepositTransaction accountDepositTransaction);

    void transfer(AccountTransferTransaction accountTransferTransaction);

    List<AccountBalance> balances();

}
