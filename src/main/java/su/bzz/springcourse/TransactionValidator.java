package su.bzz.springcourse;

import su.bzz.springcourse.exception.ValidatorException;

public class TransactionValidator {

    public void validator(FinancialTransaction financialTransaction) throws ValidatorException {
        if (financialTransaction.getSrc() < 0) {
            System.out.println("Счет отправителя не валидный");
            throw new ValidatorException();
        }
    }
}
