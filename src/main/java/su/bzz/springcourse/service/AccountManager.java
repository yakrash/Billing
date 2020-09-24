package su.bzz.springcourse.service;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import su.bzz.springcourse.dao.PostgreAccountsDAO;
import su.bzz.springcourse.model.Account;
import su.bzz.springcourse.model.FinancialTransaction;
import java.util.concurrent.TimeUnit;

@Service
public class AccountManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionLogger.class);
    private final PostgreAccountsDAO postgreAccountsDAO;
    @Value("${accountManager.expireAfterAccessDuration}")
    private int expireAfterAccessDuration;

    private final LoadingCache<Long, Account> accounts = CacheBuilder.newBuilder()
            .expireAfterAccess(expireAfterAccessDuration, TimeUnit.MINUTES)
            .maximumSize(100)
            .build(new CacheLoader<Long, Account>() {
                @Override
                public Account load(Long id) throws Exception {
                    return postgreAccountsDAO.get(id);
                }
            });

    @Autowired
    public AccountManager(PostgreAccountsDAO postgreAccountsDAO) {
        this.postgreAccountsDAO = postgreAccountsDAO;
    }

    public void createAccount() {
        long createId = postgreAccountsDAO.create(0, 0);
        accounts.put(createId, new Account(createId, 0, 0));
        LOGGER.info("Account created, id:" + createId);
    }

    public void modify(FinancialTransaction financialTransaction) {
        try {
            Account src = accounts.get(financialTransaction.getSrc());
            Account dst = accounts.get(financialTransaction.getDst());
            double amount = financialTransaction.getAmount();

            src.setCredit(src.getCredit() + amount);
            dst.setDebit(dst.getDebit() + amount);

            accounts.put(src.getId(), src);
            accounts.put(dst.getId(), dst);
        } catch (Exception e) {
            LOGGER.warn(e.toString());
        }
    }

    public Account getAccount(long id) {
        try {
            return accounts.get(id);
        } catch (Exception e) {
            LOGGER.warn(e.toString());
            return null;
        }
    }
}
