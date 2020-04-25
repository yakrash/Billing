package su.bzz.springcourse;

import org.springframework.stereotype.Service;
import su.bzz.springcourse.utils.TransactionMerger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class TransactionLogger {
    private final BlockingQueue<FinancialTransaction> loggerFinancialTransaction = new LinkedBlockingQueue<>();
    private final BlockingQueue<FinancialTransaction> tempFinancialTransaction = new LinkedBlockingQueue<>();
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private final List<FinancialTransaction> transactionsMerger = new ArrayList<>();

    public void push(FinancialTransaction financialTransaction) {
        loggerFinancialTransaction.add(financialTransaction);
    }

    public BlockingQueue<FinancialTransaction> getLoggerFinancialTransaction() {
        return loggerFinancialTransaction;
    }

    public BlockingQueue<FinancialTransaction> getTempFinancialTransaction() {
        return tempFinancialTransaction;
    }

    public List<FinancialTransaction> getTransactionsMerger() {
        return transactionsMerger;
    }

    @PostConstruct
    public void parserLoggerFT() {
        executorService.scheduleAtFixedRate(new Thread(() -> {
            loggerFinancialTransaction.drainTo(tempFinancialTransaction);
            System.out.println("В нашей заглушке: " + getTempFinancialTransaction());

            tempFinancialTransaction.drainTo(transactionsMerger);
            TransactionMerger.merge(transactionsMerger);


        }), 0, 5, TimeUnit.SECONDS);
    }

    @PreDestroy
    public void shutDownExecutorService() {
        executorService.shutdown();
    }

}
