package su.bzz.springcourse.service;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import su.bzz.springcourse.dao.PostgreAccountsDAO;
import su.bzz.springcourse.model.Account;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class AccountManager {
    private final PostgreAccountsDAO postgreAccountsDAO;

    @Value("${accountManager.expireAfterAccessDuration}")
    private int expireAfterAccessDuration;

    @Value("${accountManager.expireAfterAccessTimeUnit}")
    private String expireAfterAccessTimeUnit;

    public AccountManager(PostgreAccountsDAO postgreAccountsDAO) {
        this.postgreAccountsDAO = postgreAccountsDAO;
    }

    private final LoadingCache<Long, Account> accounts = CacheBuilder.newBuilder()
            .expireAfterAccess(expireAfterAccessDuration, TimeUnit.valueOf(expireAfterAccessTimeUnit))
            .maximumSize(100)
            .build(new CacheLoader<Long, Account>() {
                @Override
                public Account load(Long id) throws Exception {
                    return getAccountById(id);
                }
            });

    public Account getAccountById(long id) throws ExecutionException {
        return postgreAccountsDAO.get(id);
    }

    public void createAccount(double amount) {
        long createId = postgreAccountsDAO.create(amount);
        accounts.put(createId, new Account(createId, amount));
    }

    public void modify(long id, double newAmount) throws ExecutionException {
        accounts.get(id);
        accounts.put(id, new Account(id, newAmount));
    }

    public void changeAmount(long id, double amount) throws ExecutionException {
        Account acc = getAccountById(id);
        acc.setAmount(acc.getAmount() + amount);
    }


}
