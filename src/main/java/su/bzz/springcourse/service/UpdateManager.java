package su.bzz.springcourse.service;

import su.bzz.springcourse.model.FinancialTransaction;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class UpdateManager {
    private final BlockingQueue<FinancialTransaction> tempListFinancialTransaction = new LinkedBlockingQueue<>();

    public void push(List<FinancialTransaction> financialTransactionList){
        tempListFinancialTransaction.addAll(financialTransactionList);
    }


}
