package su.bzz.springcourse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BillingAPI implements iBillingAPI {

    List<FinancialTransaction> listFinancialTransaction;

    @Autowired
    public BillingAPI(List<FinancialTransaction> listFinancialTransaction) {
        this.listFinancialTransaction = listFinancialTransaction;
    }

    @Override
    public void transfer(FinancialTransaction financialTransaction) {
        if (financialTransaction.getAmount() < 0) {
            System.out.println("Error");
            System.out.println(listFinancialTransaction);
        }
    }
}
