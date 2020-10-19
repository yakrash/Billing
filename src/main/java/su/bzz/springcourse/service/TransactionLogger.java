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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class TransactionLogger {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionLogger.class);
    private final BlockingQueue<FinancialTransaction> loggerFinancialTransaction = new LinkedBlockingQueue<>();
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private final PostgresLoggerDAO postgresLoggerDAO;
    private final AccountManager accountManager;
    private final UpdateManagerAmountInDB updateManagerAmountInDB;
    private final Map<Integer, Object> mapMutex = new HashMap();

    @Autowired
    public TransactionLogger(PostgresLoggerDAO postgresLoggerDAO, AccountManager accountManager, UpdateManagerAmountInDB updateManagerAmountInDB) {
        this.postgresLoggerDAO = postgresLoggerDAO;
        this.accountManager = accountManager;
        this.updateManagerAmountInDB = updateManagerAmountInDB;
    }

    public void push(FinancialTransaction financialTransaction) {
        mapMutex.put(financialTransaction.hashCode(), new Object());
        synchronized (mapMutex.get(financialTransaction.hashCode())) {
            loggerFinancialTransaction.add(financialTransaction);

            LOGGER.info("Удерживаем транзакцию");
            try {
                mapMutex.get(financialTransaction.hashCode()).wait();
            } catch (InterruptedException e) {
                LOGGER.warn("В методе TransactionLogger.push исключение: " + e.toString());
            }
            LOGGER.info("Отпускаем транзакцию");
        }
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

            for (FinancialTransaction ft : tempFinancialTransaction) {
                synchronized (mapMutex.get(ft.hashCode())) {
                    accountManager.modify(ft);

                    mapMutex.get(ft.hashCode()).notifyAll();
                }
            }

            updateManagerAmountInDB.push(tempFinancialTransaction);

        }), 0, 3, TimeUnit.SECONDS);

    }

    @PreDestroy
    public void shutDownExecutorService() {
        executorService.shutdown();
    }
}
