package su.bzz.springcourse;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class TransactionLogger {
    private BlockingQueue<FinancialTransaction> loggerFinancialTransaction = new LinkedBlockingQueue<>();
    private BlockingQueue<FinancialTransaction> tempFinancialTransaction = new LinkedBlockingQueue<>();

    public void push(FinancialTransaction financialTransaction) {
        loggerFinancialTransaction.add(financialTransaction);
    }

    public BlockingQueue<FinancialTransaction> getLoggerFinancialTransaction() {
        return loggerFinancialTransaction;
    }

    public BlockingQueue<FinancialTransaction> getTempFinancialTransaction() {
        return tempFinancialTransaction;
    }

    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    @PostConstruct
    public void parserLoggerFT() {
        executorService.scheduleAtFixedRate(new Thread(() -> {
            loggerFinancialTransaction.drainTo(tempFinancialTransaction);
            System.out.println("В нашей заглушке: " + getTempFinancialTransaction());
        }), 0, 5, TimeUnit.SECONDS);
    }

    @PreDestroy
    public void shutDownd() {
        executorService.shutdown();
    }

}
