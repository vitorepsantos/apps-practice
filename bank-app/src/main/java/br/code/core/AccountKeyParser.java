package br.code.core;

import br.code.pojo.Account;

public interface AccountKeyParser {

    Account accountKeyToAccount(String accountKey);

}
