package su.bzz.springcourse;

import su.bzz.springcourse.model.FinancialTransaction;

public interface BillingAPI {
    void transfer(FinancialTransaction financialTransaction);
}
