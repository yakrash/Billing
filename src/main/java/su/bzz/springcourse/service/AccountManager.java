package su.bzz.springcourse.service;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import su.bzz.springcourse.model.Account;

import java.util.concurrent.ExecutionException;

public class AccountManager {

    LoadingCache<Integer, Account> accounts = CacheBuilder.newBuilder()
            .build(new CacheLoader<Integer, Account>() {
                @Override
                public Account load(Integer id) throws Exception {
                    return getAccountById(id);
                }
            });

    public Account getAccountById(int id) throws ExecutionException {
        return accounts.get(id);
    }

    public void createAccount(int id, double amount) {
        accounts.put(id, new Account(id, amount));
    }

    public void changeAmount(int id, double amount) throws ExecutionException {
        getAccountById(id).setAmount(getAccountById(id).getAmount() + amount);
    }

}
