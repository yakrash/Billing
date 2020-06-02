package su.bzz.springcourse.dao;

import su.bzz.springcourse.model.Account;
import su.bzz.springcourse.model.FinancialTransaction;

import java.util.List;

public interface AccountsDAO {
    long create(double debit, double credit);

    Account get(long id);

    void modify(List<Account> accountList);

}
