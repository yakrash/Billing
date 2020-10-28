package dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import su.bzz.springcourse.dao.PostgreAccountsDAO;
import su.bzz.springcourse.model.Account;
import su.bzz.springcourse.model.FinancialTransaction;

import javax.sql.DataSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PostgreAccountsDAOTest {
    private static PostgreAccountsDAO postgreAccountsDAO;
    private static JdbcTemplate jdbcTemplate;
    String sql = "INSERT INTO accounts(debit, credit) values(?, ?)";

    @Before
    public void before() {
        DataSource dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:accounts.sql")
                .build();
        jdbcTemplate = new JdbcTemplate(dataSource);
        postgreAccountsDAO = new PostgreAccountsDAO(jdbcTemplate);
    }

    @After
    public void after() {
        jdbcTemplate.execute("drop table accounts");
    }

    @Test
    public void methodCreateTest() {
        Account accountNewTest = new Account(1, 10, 20);
        long count = postgreAccountsDAO.create(10, 20);
        assertEquals(1, count);
        assertEquals(accountNewTest, postgreAccountsDAO.get(1));
    }

    @Test
    public void methodGetTestAccountValid() {
        jdbcTemplate.update(sql, 100, 200);
        Account account = postgreAccountsDAO.get(1);
        Account accountNotNull = new Account(1, 100, 200);
        assertEquals(accountNotNull, account);
    }

    @Test
    public void methodGetTestAccountNotFound() {
        Account account = postgreAccountsDAO.get(99);
        assertNull(account);
    }

    @Test
    public void checkSumInMethodModify() {
        jdbcTemplate.update(sql, 10, 20);
        jdbcTemplate.update(sql, 40, 50);

        List<FinancialTransaction> financialTransactionList = new ArrayList<>();
        financialTransactionList.add(new FinancialTransaction(1, 2, 100));
        financialTransactionList.add(new FinancialTransaction(2, 1, 50));
        postgreAccountsDAO.modify(financialTransactionList);

        Account account1 = postgreAccountsDAO.get(1);
        Account account2 = postgreAccountsDAO.get(2);
        Account account1AfterModify = new Account(1, 60, 120);
        Account account2AfterModify = new Account(2, 140, 100);

        assertEquals(account1AfterModify, account1);
        assertEquals(account2AfterModify, account2);
    }
}

