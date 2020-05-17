package su.bzz.springcourse.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import su.bzz.springcourse.dao.PostgresLoggerDAO;
import su.bzz.springcourse.model.FinancialTransaction;
import su.bzz.springcourse.utils.MergeFinancialTransactions;

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
    private final List<FinancialTransaction> mergedFinancialTransactions = new ArrayList<>();
    private final PostgresLoggerDAO postgresLoggerDAO;
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionLogger.class);
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
            LOGGER.info("1. TempFT: " + tempFinancialTransaction);

            tempFinancialTransaction = MergeFinancialTransactions.merge(tempFinancialTransaction);
            LOGGER.info("2. MergeTempFT: " + tempFinancialTransaction);

            postgresLoggerDAO.insert(tempFinancialTransaction);
            mergedFinancialTransactions.addAll(tempFinancialTransaction);
            LOGGER.info("3. MergedFT" + mergedFinancialTransactions);

        }), 0, 8, TimeUnit.SECONDS);
    }

    @PreDestroy
    public void shutDownExecutorService() {
        executorService.shutdown();
    }

}
