package su.bzz.springcourse.dao;

import su.bzz.springcourse.model.Account;

public interface AccountsDAO {
    void create(double amount);

    Account get(int id);


}
