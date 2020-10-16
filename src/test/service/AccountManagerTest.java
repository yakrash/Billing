package service;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import su.bzz.springcourse.dao.PostgreAccountsDAO;
import su.bzz.springcourse.model.Account;
import su.bzz.springcourse.model.FinancialTransaction;
import su.bzz.springcourse.service.AccountManager;

import javax.sql.DataSource;

public class AccountManagerTest {
    AccountManager accountManager;

    @BeforeClass
    public void beforeClass() {
        DataSource dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:accounts.sql")
                .build();

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        PostgreAccountsDAO postgreAccountsDAO = new PostgreAccountsDAO(jdbcTemplate);
        accountManager = new AccountManager(postgreAccountsDAO);

    }

    @Test
    public void methodModifyCheckSum() {
        FinancialTransaction financialTransaction = new FinancialTransaction(1, 2, 100);
        Account account1 = new Account(1, 10, 20);

    }
}
