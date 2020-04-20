import org.junit.Assert;
import org.junit.jupiter.api.Test;
import su.bzz.springcourse.FinancialTransaction;
import su.bzz.springcourse.TransactionValidator;


public class TestTransactionValidator {
    private FinancialTransaction financialTransaction;
    TransactionValidator transactionValidator = new TransactionValidator();

    @Test
    public void validatorInvalidSource() {
        financialTransaction = new FinancialTransaction(-111, 222, 333.3);
        boolean check = transactionValidator.validator(financialTransaction);
        Assert.assertEquals(check, false);
    }

}
