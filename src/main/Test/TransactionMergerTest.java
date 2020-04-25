import org.junit.Assert;
import org.junit.jupiter.api.Test;
import su.bzz.springcourse.FinancialTransaction;
import su.bzz.springcourse.TransactionLogger;
import su.bzz.springcourse.utils.TransactionMerger;

import java.util.Map;

public class TransactionMergerTest {
    TransactionLogger transactionLogger = new TransactionLogger();
    private Map<FinancialTransaction, Double> testMap;

    @Test
    public void validWorkingMerge() {
        transactionLogger.getTransactionsMerger().add(new FinancialTransaction(1, 2, 10));
        transactionLogger.getTransactionsMerger().add(new FinancialTransaction(1, 2, 50));
        transactionLogger.getTransactionsMerger().add(new FinancialTransaction(1, 2, 40));

        transactionLogger.getTransactionsMerger().add(new FinancialTransaction(1, 3, 10));
        transactionLogger.getTransactionsMerger().add(new FinancialTransaction(1, 3, 10));

        transactionLogger.getTransactionsMerger().add(new FinancialTransaction(2, 1, 10));
        transactionLogger.getTransactionsMerger().add(new FinancialTransaction(2, 1, 50));
        transactionLogger.getTransactionsMerger().add(new FinancialTransaction(2, 1, 40));

        transactionLogger.getTransactionsMerger().add(new FinancialTransaction(3, 2, 10));
        transactionLogger.getTransactionsMerger().add(new FinancialTransaction(1, 3, 50));

        testMap = TransactionMerger.merge(transactionLogger.getTransactionsMerger());

        Assert.assertEquals(testMap.get(new FinancialTransaction(1, 2, 10)), 100.0, 1e-9);
        Assert.assertEquals(testMap.get(new FinancialTransaction(1, 3, 10)), 70.0, 1e-9);
        Assert.assertEquals(testMap.get(new FinancialTransaction(2, 1, 1000)), 100.0, 1e-9);
        Assert.assertEquals(testMap.get(new FinancialTransaction(3, 2, 10)), 10.0, 1e-9);
    }
}
