package su.bzz.springcourse.dao;

import su.bzz.springcourse.model.Account;

public interface AccountsDAO {
    long create(double debit, double credit);

    Account get(long id);

}
