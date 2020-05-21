package su.bzz.springcourse.service;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;
import su.bzz.springcourse.dao.AccountsDAOImp;
import su.bzz.springcourse.model.Account;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class AccountManager {
    private final AccountsDAOImp accountsDAOImp;

    public AccountManager(AccountsDAOImp accountsDAOImp) {
        this.accountsDAOImp = accountsDAOImp;
    }

    private final LoadingCache<Long, Account> accounts = CacheBuilder.newBuilder()
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .maximumSize(100)
            .build(new CacheLoader<Long, Account>() {
                @Override
                public Account load(Long id) throws Exception {
                    return getAccountById(id);
                }
            });

    public Account getAccountById(long id) throws ExecutionException {
        return accountsDAOImp.get(id);
    }

    public void createAccount(double amount) {
        long createId = accountsDAOImp.create(amount);
        accounts.put(createId, new Account(createId, amount));
    }

    public void modify(long id, double newAmount) throws ExecutionException {
        accounts.get(id);
        accounts.put(id, new Account(id, newAmount));
    }

    public void changeAmount(long id, double amount) throws ExecutionException {
        getAccountById(id).setAmount(getAccountById(id).getAmount() + amount);
    }

//    @PostConstruct
//    public void testCode() throws ExecutionException {
//        createAccount(2000);
//        System.out.println(accounts.get(2L));
//        modify(1, 500);
//        System.out.println(accounts.get(1L));
//
//    }

}
