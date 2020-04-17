package su.bzz.springcourse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BillingAPI implements BillingAPIImpl {

    private List<FinancialTransaction> listFinancialTransaction = new ArrayList<>();

    @Override
    public void transfer(FinancialTransaction financialTransaction) {
        if (financialTransaction.getAmount() < 0) {
            System.out.println("Error");
            System.out.println(listFinancialTransaction);
        }
    }

    public void listAdd (FinancialTransaction financialTransaction){
        listFinancialTransaction.add(financialTransaction);
    }

    public List<FinancialTransaction> getList (){
        return listFinancialTransaction;
    }
}
