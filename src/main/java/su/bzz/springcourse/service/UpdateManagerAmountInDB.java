package su.bzz.springcourse.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import su.bzz.springcourse.dao.PostgreAccountsDAO;
import su.bzz.springcourse.model.FinancialTransaction;

import javax.annotation.PostConstruct;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class UpdateManagerAmountInDB {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionLogger.class);
    private final BlockingQueue<FinancialTransaction> tempBlockingQueueFinancialTransaction = new LinkedBlockingQueue<>();
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private final PostgreAccountsDAO postgreAccountsDAO;

    @Autowired
    public UpdateManagerAmountInDB(PostgreAccountsDAO postgreAccountsDAO) {
        this.postgreAccountsDAO = postgreAccountsDAO;
    }

    public void push(List<FinancialTransaction> financialTransactionList) {
        tempBlockingQueueFinancialTransaction.addAll(financialTransactionList);
    }

    @PostConstruct
    public void addToDB() {
        executorService.scheduleAtFixedRate(new Thread(() -> {
            List<FinancialTransaction> tempFinancialTransactionList = new ArrayList<>();

            tempBlockingQueueFinancialTransaction.drainTo(tempFinancialTransactionList);
            LOGGER.info("1. FinancialTransactionInDB: " + tempFinancialTransactionList);

            try {
                postgreAccountsDAO.modify(tempFinancialTransactionList);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }), 0, 20, TimeUnit.SECONDS);
    }
}
