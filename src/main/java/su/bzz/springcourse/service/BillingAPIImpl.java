package su.bzz.springcourse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import su.bzz.springcourse.model.Account;
import su.bzz.springcourse.model.FinancialTransaction;
import su.bzz.springcourse.utils.TransactionValidator;

@Service
public class BillingAPIImpl implements BillingAPI {

    private final TransactionValidator transactionValidator = new TransactionValidator();
    private final TransactionLogger transactionLogger;
    private AccountManager accountManager;

    @Autowired
    public BillingAPIImpl(TransactionLogger transactionLogger, AccountManager accountManager) {
        this.transactionLogger = transactionLogger;
        this.accountManager = accountManager;
    }

    @Override
    public void transfer(FinancialTransaction financialTransaction) {

        if (transactionValidator.validator(financialTransaction)) {
            transactionLogger.push(financialTransaction);
        }
    }

    @Override
    public void createAccount() {
        accountManager.createAccount();
    }

    @Override
    public Account getAccount(long id) {
        return accountManager.getAccount(id);
    }
}
