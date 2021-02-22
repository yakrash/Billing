package su.bzz.springcourse.service;

import javafx.util.Pair;
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
import java.util.stream.Collectors;

@Service
public class TransactionLogger {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionLogger.class);
    private final BlockingQueue<Pair<FinancialTransaction, Object>> loggerFinancialTransaction =
            new LinkedBlockingQueue<>();
    private final ScheduledExecutorService executorService = Executors
            .newSingleThreadScheduledExecutor();
    private final PostgresLoggerDAO postgresLoggerDAO;
    private final AccountManager accountManager;
    private final UpdateManagerAmountInDB updateManagerAmountInDB;

    @Autowired
    public TransactionLogger(PostgresLoggerDAO postgresLoggerDAO, AccountManager accountManager,
                             UpdateManagerAmountInDB updateManagerAmountInDB) {
        this.postgresLoggerDAO = postgresLoggerDAO;
        this.accountManager = accountManager;
        this.updateManagerAmountInDB = updateManagerAmountInDB;
    }

    public void push(FinancialTransaction financialTransaction) {

        loggerFinancialTransaction.add(new Pair<>(financialTransaction, new Object()));
        LOGGER.info("Удерживаем транзакцию");

        Object mutex = loggerFinancialTransaction.peek().getValue();
        synchronized (mutex) {
            try {
                mutex.wait();
            } catch (InterruptedException e) {
                LOGGER.warn("В методе TransactionLogger.push исключение: " + e.toString());
            }
            LOGGER.info("Отпускаем транзакцию");
        }
    }

    @PostConstruct
    public void parserLoggerFT() {

        executorService.scheduleAtFixedRate(new Thread(() -> {
            List<Pair<FinancialTransaction, Object>> tempFinancialTransactionPair = new ArrayList<>();
            List<FinancialTransaction> tempFinancialTransaction;

            loggerFinancialTransaction.drainTo(tempFinancialTransactionPair);
            LOGGER.info("1. TempFT: " + tempFinancialTransactionPair);

            tempFinancialTransaction = tempFinancialTransactionPair.stream().map(Pair::getKey)
                    .collect(Collectors.toList());

            tempFinancialTransaction = MergeFinancialTransactions.merge(tempFinancialTransaction);
            LOGGER.info("2. MergeTempFT: " + tempFinancialTransaction);

            postgresLoggerDAO.insert(tempFinancialTransaction);

            for (FinancialTransaction ft : tempFinancialTransaction) {
                accountManager.modify(ft);
            }
            updateManagerAmountInDB.push(tempFinancialTransaction);

            List<Object> mutexList = tempFinancialTransactionPair.stream().map(Pair::getValue)
                    .collect(Collectors.toList());
            for (Object mutex : mutexList) {
                synchronized (mutex) {
                    mutex.notifyAll();
                }
            }
        }), 0, 3, TimeUnit.SECONDS);

    }

    @PreDestroy
    public void shutDownExecutorService() {
        executorService.shutdown();
    }
}
