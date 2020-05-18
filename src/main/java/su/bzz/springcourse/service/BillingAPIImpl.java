package su.bzz.springcourse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import su.bzz.springcourse.utils.TransactionValidator;
import su.bzz.springcourse.model.FinancialTransaction;

@Service
public class BillingAPIImpl implements BillingAPI {

    private final TransactionValidator transactionValidator = new TransactionValidator();
    private final TransactionLogger transactionLogger;

    @Autowired
    public BillingAPIImpl(TransactionLogger transactionLogger) {
        this.transactionLogger = transactionLogger;
    }

    @Override
    public void transfer(FinancialTransaction financialTransaction) {

        if (transactionValidator.validator(financialTransaction)) {
            transactionLogger.push(financialTransaction);
        }
    }
}
