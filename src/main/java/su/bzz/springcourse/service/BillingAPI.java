package su.bzz.springcourse.service;

import su.bzz.springcourse.model.FinancialTransaction;

public interface BillingAPI {
    void transfer(FinancialTransaction financialTransaction);
}
