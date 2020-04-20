package su.bzz.springcourse;

import org.graalvm.compiler.lir.alloc.trace.TraceBuilderPhase;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BillingAPIImpl implements BillingAPI {

    private TransactionValidator transactionValidator = new TransactionValidator();
    private TransactionLogger transactionLogger;

    public BillingAPIImpl (TransactionLogger transactionLogger){
        this.transactionLogger = transactionLogger;
    }
    @Override
    public void transfer(FinancialTransaction financialTransaction) {

        if (transactionValidator.validator(financialTransaction)) {
            transactionLogger.push(financialTransaction);
        }
    }
}
