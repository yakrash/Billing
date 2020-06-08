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

//            List<Account> accountList = convertFinancialTransactionToAccountList(tempFinancialTransactionList);
//            LOGGER.info("2. accountList: " + accountList);
            addToDBFromList(tempFinancialTransactionList);
            long timeEnd = System.currentTimeMillis();
            System.out.println("Execution time - " + (timeEnd - timeStart));
        }), 0, 20, TimeUnit.SECONDS);
    }

//    private List<Account> convertFinancialTransactionToAccountList(List<FinancialTransaction> financialTransactionList) {
//        Map<Long, Account> accountMap = new HashMap<>();
//        for (FinancialTransaction e : financialTransactionList) {
//            Long dstAccountId = e.getDst();
//            Long srcAccountId = e.getSrc();
//
//            if (accountMap.containsKey(dstAccountId)) {
//                Account account = accountMap.get(dstAccountId);
//                account.setDebit(account.getDebit() + e.getAmount());
//                accountMap.put(dstAccountId, account);
//            } else {
//                Account account = new Account(dstAccountId, e.getAmount(), 0);
//                accountMap.put(dstAccountId, account);
//            }
//
//            if (accountMap.containsKey(srcAccountId)) {
//                Account account = accountMap.get(srcAccountId);
//                account.setCredit(account.getCredit() + e.getAmount());
//                accountMap.put(srcAccountId, account);
//            } else {
//                Account account = new Account(srcAccountId, 0, e.getAmount());
//                accountMap.put(srcAccountId, account);
//            }
//        }
//
//        return new ArrayList<>(accountMap.values());
//    }

    private void addToDBFromList(List<FinancialTransaction> financialTransactionsList) {
        for (FinancialTransaction e : financialTransactionsList) {
            Account accountDstFromDB = postgreAccountsDAO.get(e.getDst());
            Account accountSrcFromDB = postgreAccountsDAO.get(e.getSrc());

            accountSrcFromDB.setDebit(accountSrcFromDB.getDebit() + e.getAmount());
            accountDstFromDB.setCredit(accountDstFromDB.getCredit() + e.getAmount());

            postgreAccountsDAO.modify(accountDstFromDB);
            postgreAccountsDAO.modify(accountSrcFromDB);
        }
    }


}
