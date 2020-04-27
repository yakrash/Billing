import org.junit.Assert;
import org.junit.jupiter.api.Test;
import su.bzz.springcourse.FinancialTransaction;
import su.bzz.springcourse.utils.TransactionMerger;

import java.util.ArrayList;
import java.util.List;

public class TransactionMergerTest {
    private ArrayList<FinancialTransaction> mergeList;
    private final List<FinancialTransaction> testList = new ArrayList<>();

    @Test
    public void validWorkingMerge() {
        FinancialTransaction test1 = new FinancialTransaction(1, 2, 10);
        testList.add(new FinancialTransaction(1, 2, 10));
        testList.add(new FinancialTransaction(1, 2, 50));
        testList.add(new FinancialTransaction(1, 2, 40));

        FinancialTransaction test2 = new FinancialTransaction(1, 3, 10);
        testList.add(new FinancialTransaction(1, 3, 10));
        testList.add(new FinancialTransaction(1, 3, 10));

        FinancialTransaction test3 = new FinancialTransaction(2, 1, 10);
        testList.add(new FinancialTransaction(2, 1, 10));
        testList.add(new FinancialTransaction(2, 1, 50));
        testList.add(new FinancialTransaction(2, 1, 40));

        FinancialTransaction test4 = new FinancialTransaction(3, 2, 10);
        testList.add(new FinancialTransaction(3, 2, 10));
        testList.add(new FinancialTransaction(1, 3, 50));

        mergeList = TransactionMerger.merge(testList);

        Assert.assertEquals(mergeList.get(mergeList.indexOf(test1)).getAmount(), 100.0, 1e-9);
        Assert.assertEquals(mergeList.get(mergeList.indexOf(test2)).getAmount(), 70.0, 1e-9);
        Assert.assertEquals(mergeList.get(mergeList.indexOf(test3)).getAmount(), 100.0, 1e-9);
        Assert.assertEquals(mergeList.get(mergeList.indexOf(test4)).getAmount(), 10.0, 1e-9);
    }
}
