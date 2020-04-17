package su.bzz.springcourse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import su.bzz.springcourse.exception.ValidatorException;

import java.util.ArrayList;
import java.util.List;

@Component
public class BillingAPI implements BillingAPIImpl {

    private List<FinancialTransaction> listFinancialTransaction = new ArrayList<>();
    TransactionValidator transactionValidator = new TransactionValidator();

    @Override
    public void transfer(FinancialTransaction financialTransaction) {
        try {
            transactionValidator.validator(financialTransaction);
        } catch (ValidatorException e) {
            e.printStackTrace();
        }
        System.out.println(listFinancialTransaction);


    }

    public void listAdd(FinancialTransaction financialTransaction) {
        listFinancialTransaction.add(financialTransaction);
    }

    public List<FinancialTransaction> getList() {
        return listFinancialTransaction;
    }
}
