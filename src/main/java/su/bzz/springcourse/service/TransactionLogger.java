package su.bzz.springcourse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import su.bzz.springcourse.dao.PostgresLoggerDAO;
import su.bzz.springcourse.model.FinancialTransaction;
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
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private List<FinancialTransaction> mergedFinancialTransactions = new ArrayList<>();
    private PostgresLoggerDAO postgresLoggerDAO;

    @Autowired
    public TransactionLogger(PostgresLoggerDAO postgresLoggerDAO) {
        this.postgresLoggerDAO = postgresLoggerDAO;
    }

    public BlockingQueue<FinancialTransaction> getLoggerFinancialTransaction() {
        return loggerFinancialTransaction;
    }

    public void push(FinancialTransaction financialTransaction) {
        loggerFinancialTransaction.add(financialTransaction);
    }

    @PostConstruct
    public void parserLoggerFT() {

        executorService.scheduleAtFixedRate(new Thread(() -> {
            List<FinancialTransaction> tempFinancialTransaction = new ArrayList<>();

            loggerFinancialTransaction.drainTo(tempFinancialTransaction);
            System.out.println("В нашей заглушке: " + tempFinancialTransaction);

            tempFinancialTransaction = TransactionMerger.merge(tempFinancialTransaction);
            System.out.println("TempFT: " + tempFinancialTransaction);

            postgresLoggerDAO.insert(tempFinancialTransaction);
            mergedFinancialTransactions.addAll(tempFinancialTransaction);
            System.out.println("mergedFT" + mergedFinancialTransactions);

        }), 0, 8, TimeUnit.SECONDS);
    }

    @PreDestroy
    public void shutDownExecutorService() {
        executorService.shutdown();
    }

}
