import org.junit.Assert;
import org.junit.jupiter.api.Test;
import su.bzz.springcourse.FinancialTransaction;
import su.bzz.springcourse.TransactionValidator;


public class TransactionValidatorTest {
    private FinancialTransaction financialTransaction;
    private TransactionValidator transactionValidator = new TransactionValidator();

    @Test
    public void validatorInvalidSource() {
        financialTransaction = new FinancialTransaction(-111, 222, 333.3);
        boolean check = transactionValidator.validator(financialTransaction);
        Assert.assertFalse(check);
    }

    @Test
    public void validatorInvalidDst() {
        financialTransaction = new FinancialTransaction(111, -222, 333.3);
        boolean check = transactionValidator.validator(financialTransaction);
        Assert.assertFalse(check);
    }

    @Test
    public void validatorInvalidSrcDst() {
        financialTransaction = new FinancialTransaction(-111, -222, 333.3);
        boolean check = transactionValidator.validator(financialTransaction);
        Assert.assertFalse(check);
    }

    @Test
    public void validatorInvalidSrcAmount() {
        financialTransaction = new FinancialTransaction(-111, 222, -333.3);
        boolean check = transactionValidator.validator(financialTransaction);
        Assert.assertFalse(check);
    }

    @Test
    public void validatorInvalidDstAmount() {
        financialTransaction = new FinancialTransaction(111, -222, -333.3);
        boolean check = transactionValidator.validator(financialTransaction);
        Assert.assertFalse(check);
    }

    @Test
    public void validatorInvalidAll() {
        financialTransaction = new FinancialTransaction(-111, -222, -333.3);
        boolean check = transactionValidator.validator(financialTransaction);
        Assert.assertFalse(check);
    }

    @Test
    public void validatorValid() {
        financialTransaction = new FinancialTransaction(111, 222, 333.3);
        boolean check = transactionValidator.validator(financialTransaction);
        Assert.assertTrue(check);
    }

}
