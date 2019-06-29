package br.code.core;

import br.code.pojo.Account;

public class AccountKeyParserDefault implements AccountKeyParser {

    @Override
    public Account accountKeyToAccount(String accountKey) {
        String[] parts = accountKey.split("-");
        return new Account(parts[0], parts[1], parts[2]);
    }

}
