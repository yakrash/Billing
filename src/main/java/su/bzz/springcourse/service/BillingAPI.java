package su.bzz.springcourse.service;

import su.bzz.springcourse.model.Account;
import su.bzz.springcourse.model.FinancialTransaction;

public interface BillingAPI {
    void transfer(FinancialTransaction financialTransaction);

    void createAccount();

    Account getAccount(long id);
}
