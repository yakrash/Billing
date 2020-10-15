package dao;

import org.junit.BeforeClass;
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

    @BeforeClass
    public static void beforeClass() {
        DataSource dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:accounts.sql")
                .build();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        postgreAccountsDAO = new PostgreAccountsDAO(jdbcTemplate);

        String sql = "INSERT INTO accounts(debit, credit) values(?, ?)";
        jdbcTemplate.update(sql, 10, 20);
        jdbcTemplate.update(sql, 40, 50);
        jdbcTemplate.update(sql, 10, 20);
    }

//    @Test
//    public void methodCreateTest(){
//        long count = postgreAccountsDAO.create(10, 20);
//        assertEquals(1, count);
//    }

    @Test
    public void methodGetTestAccountValid() {
        Account account = postgreAccountsDAO.get(3);
        Account accountNotNull = new Account(3, 10, 20);
        assertEquals(accountNotNull, account);
    }

    @Test
    public void methodGetTestAccountNotFound() {
        Account account = postgreAccountsDAO.get(4);
        assertNull(account);
    }

    @Test
    public void checkSumInMethodModify() {
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

