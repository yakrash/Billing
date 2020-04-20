package su.bzz.springcourse;

import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class TransactionLogger {
    private BlockingQueue<FinancialTransaction> loggerFT = new LinkedBlockingQueue<>();
    private BlockingQueue<FinancialTransaction> tempFT = new LinkedBlockingQueue<>();

    public void push (FinancialTransaction financialTransaction){
        loggerFT.add(financialTransaction);
    }

    public BlockingQueue<FinancialTransaction> getLoggerFT() {
        return loggerFT;
    }

    public BlockingQueue<FinancialTransaction> getTempFT() {
        return tempFT;
    }
}
