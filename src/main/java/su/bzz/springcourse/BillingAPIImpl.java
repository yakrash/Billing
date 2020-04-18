package su.bzz.springcourse;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BillingAPIImpl implements BillingAPI {

    private List<FinancialTransaction> listFinancialTransaction = new ArrayList<>();
    private TransactionValidator transactionValidator = new TransactionValidator();

    @Override
    public void transfer(FinancialTransaction financialTransaction) {

        if (transactionValidator.validator(financialTransaction)) {
            listFinancialTransaction.add(financialTransaction);
        }
    }

    public List<FinancialTransaction> getList() {
        return listFinancialTransaction;
    }
}
