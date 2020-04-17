package su.bzz.springcourse;

import java.util.logging.Logger;

public class TransactionValidator {

    private final static Logger logger = Logger.getLogger(TransactionValidator.class.getName());
    public boolean validator(FinancialTransaction financialTransaction) {
        if (financialTransaction.getSrc() < 0) {
            logger.info("Счет отправителя не валидный");
            return false;
        }
        if (financialTransaction.getDst() < 0) {
            logger.info("Счет получателя не валидный");
            return false;
        }
        if (financialTransaction.getAmount() < 0) {
            logger.info("Сумма перевода не валидная");
            return false;
        }
        return true;
    }
}
