package su.bzz.springcourse.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import su.bzz.springcourse.dao.PostgreAccountsDAO;
import su.bzz.springcourse.model.Account;
import su.bzz.springcourse.model.FinancialTransaction;
import su.bzz.springcourse.utils.MergeFinancialTransactions;

import javax.annotation.PostConstruct;
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
public class UpdateManager {
    private final BlockingQueue<FinancialTransaction> tempBlockingQueueFinancialTransaction = new LinkedBlockingQueue<>();
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private final PostgreAccountsDAO postgreAccountsDAO;
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionLogger.class);

    public UpdateManager(PostgreAccountsDAO postgreAccountsDAO) {
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
            LOGGER.info("1. BeforeMerge: " + tempFinancialTransactionList);

            List<Account> accountList = convertFinancialTransactionToAccountList(tempFinancialTransactionList);
            LOGGER.info("2. accountList: " + accountList);
            addToDBFromList(accountList);
        }), 0, 15, TimeUnit.SECONDS);
    }

    private List<Account> convertFinancialTransactionToAccountList(List<FinancialTransaction> financialTransactionList) {
        Map<Long, Account> accountMap = new HashMap<>();
        for (FinancialTransaction e : financialTransactionList) {
            Long dstAccountNumber = e.getDst();
            Long srcAccountNumber = e.getSrc();

            if (accountMap.containsKey(dstAccountNumber)) {
                Account account = accountMap.get(dstAccountNumber);
                account.setDebit(account.getDebit() + e.getAmount());
                accountMap.put(dstAccountNumber, account);
            } else {
                Account account = new Account(dstAccountNumber, e.getAmount(), 0);
                accountMap.put(dstAccountNumber, account);
            }

            if (accountMap.containsKey(srcAccountNumber)) {
                Account account = accountMap.get(srcAccountNumber);
                account.setCredit(account.getCredit() + e.getAmount());
                accountMap.put(srcAccountNumber, account);
            } else {
                Account account = new Account(srcAccountNumber, 0, e.getAmount());
                accountMap.put(srcAccountNumber, account);
            }
        }

        return new ArrayList<>(accountMap.values());
    }

    private void addToDBFromList(List<Account> accountList) {
        for (Account account : accountList) {
            Account accountFromDB = postgreAccountsDAO.get(account.getId());
            account.setDebit(account.getDebit() + accountFromDB.getDebit());
            account.setCredit(account.getCredit() + accountFromDB.getCredit());
            postgreAccountsDAO.modify(account);
        }
    }


}
