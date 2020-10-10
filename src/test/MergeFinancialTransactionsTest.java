import org.junit.Assert;
import org.junit.jupiter.api.Test;
import su.bzz.springcourse.model.FinancialTransaction;
import su.bzz.springcourse.utils.MergeFinancialTransactions;

import java.util.ArrayList;
import java.util.List;

public class MergeFinancialTransactionsTest {
    private List<FinancialTransaction> mergeList = new ArrayList<>();
    private final List<FinancialTransaction> testList = new ArrayList<>();

    @Test
    public void validWorkingMerge2()  {
        testList.add(new FinancialTransaction(1, 2, 10));
        testList.add(new FinancialTransaction(1, 2, 50));
        testList.add(new FinancialTransaction(1, 2, 40));
        testList.add(new FinancialTransaction(1, 3, 10));
        testList.add(new FinancialTransaction(1, 3, 10));
        testList.add(new FinancialTransaction(2, 1, 10));
        testList.add(new FinancialTransaction(2, 1, 50));
        testList.add(new FinancialTransaction(2, 1, 40));
        testList.add(new FinancialTransaction(3, 2, 10));
        testList.add(new FinancialTransaction(1, 3, 50));

        mergeList = MergeFinancialTransactions.merge(testList);

        Assert.assertEquals(mergeList.get(0).getAmount(), 100.0, 1e-9);
        Assert.assertEquals(mergeList.get(1).getAmount(), 100.0, 1e-9);
        Assert.assertEquals(mergeList.get(2).getAmount(), 70.0, 1e-9);
        Assert.assertEquals(mergeList.get(3).getAmount(), 10.0, 1e-9);
    }
}
