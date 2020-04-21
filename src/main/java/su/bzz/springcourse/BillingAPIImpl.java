package su.bzz.springcourse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BillingAPIImpl implements BillingAPI {

    private TransactionValidator transactionValidator = new TransactionValidator();
    private TransactionLogger transactionLogger;

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
