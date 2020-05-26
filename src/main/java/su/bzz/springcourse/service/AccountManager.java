package su.bzz.springcourse.service;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import su.bzz.springcourse.dao.PostgreAccountsDAO;
import su.bzz.springcourse.model.Account;

import java.util.concurrent.TimeUnit;

@Service
public class AccountManager {
    private final PostgreAccountsDAO postgreAccountsDAO;

    @Value("${accountManager.expireAfterAccessDuration}")
    private int expireAfterAccessDuration;

//    @Value("${accountManager.expireAfterAccessTimeUnit}")
//    private TimeUnit expireAfterAccessTimeUnit;

    private final LoadingCache<Long, Account> accounts = CacheBuilder.newBuilder()
            .expireAfterAccess(expireAfterAccessDuration, TimeUnit.MINUTES)
            .maximumSize(100)
            .build(new CacheLoader<Long, Account>() {
                @Override
                public Account load(Long id) throws Exception {
                    return postgreAccountsDAO.get(id);
                }
            });

    public AccountManager(PostgreAccountsDAO postgreAccountsDAO) {
        this.postgreAccountsDAO = postgreAccountsDAO;
    }

    public void createAccount(double debit, double credit) {
        long createId = postgreAccountsDAO.create(debit, credit);
        accounts.put(createId, new Account(createId, debit, credit));
    }


}
