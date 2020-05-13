package su.bzz.springcourse.dao;

import su.bzz.springcourse.model.FinancialTransaction;

import java.util.List;

public interface FinancialTransactionDao {
    public void insert(List<FinancialTransaction> financialTransactionList);
}
