package su.bzz.springcourse.dao;

import su.bzz.springcourse.model.Account;

public interface AccountsDAO {
    long create(double amount);

    Account get(long id);


}
