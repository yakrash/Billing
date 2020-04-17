package su.bzz.springcourse;

import su.bzz.springcourse.exception.ValidatorException;

import java.util.logging.Logger;

public class TransactionValidator {
    private final static Logger logger = Logger.getLogger(TransactionValidator.class.getName());
    public void validator(FinancialTransaction financialTransaction) throws ValidatorException {
        if (financialTransaction.getSrc() < 0) {
            logger.info("Счет отправителя не валидный");
            throw new ValidatorException();
        }
        if (financialTransaction.getDst() < 0) {
            logger.info("Счет получателя не валидный");
            throw new ValidatorException();
        }
        if (financialTransaction.getAmount() < 0) {
            logger.info("Сумма перевода не валидная");
            throw new ValidatorException();
        }
    }
}
