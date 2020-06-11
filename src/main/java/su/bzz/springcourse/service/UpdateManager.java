package su.bzz.springcourse.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import su.bzz.springcourse.dao.PostgreAccountsDAO;
import su.bzz.springcourse.model.FinancialTransaction;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class UpdateManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionLogger.class);
    private final BlockingQueue<FinancialTransaction> tempBlockingQueueFinancialTransaction = new LinkedBlockingQueue<>();
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private final PostgreAccountsDAO postgreAccountsDAO;

    public UpdateManager(PostgreAccountsDAO postgreAccountsDAO) {
        this.postgreAccountsDAO = postgreAccountsDAO;
    }

    public void push(List<FinancialTransaction> financialTransactionList) {
        tempBlockingQueueFinancialTransaction.addAll(financialTransactionList);
    }

    @PostConstruct
    public void addToDB() {
        executorService.scheduleAtFixedRate(new Thread(() -> {
            long timeStart = System.currentTimeMillis();
            List<FinancialTransaction> tempFinancialTransactionList = new ArrayList<>();

            tempBlockingQueueFinancialTransaction.drainTo(tempFinancialTransactionList);
            LOGGER.info("1. FinancialTransaction: " + tempFinancialTransactionList);

            postgreAccountsDAO.modify(tempFinancialTransactionList);
            long timeEnd = System.currentTimeMillis();
            LOGGER.info("Execution time - " + (timeEnd - timeStart));
        }), 0, 20, TimeUnit.SECONDS);
    }
}
