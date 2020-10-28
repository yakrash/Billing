package dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import su.bzz.springcourse.dao.PostgresLoggerDAO;
import su.bzz.springcourse.model.FinancialTransaction;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PostgresLoggerDAOTest {
    private static JdbcTemplate jdbcTemplate;

    @Before
    public void before() {
        DataSource dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:financial_transaction.sql")
                .build();

        jdbcTemplate = new JdbcTemplate(dataSource);
        PostgresLoggerDAO postgresLoggerDAO = new PostgresLoggerDAO(jdbcTemplate);

        List<FinancialTransaction> financialTransactionList = new ArrayList<>();
        financialTransactionList.add(new FinancialTransaction(1, 10, 20));
        financialTransactionList.add(new FinancialTransaction(2, 20, 40));
        postgresLoggerDAO.insert(financialTransactionList);
    }

    @After
    public void after() {
        jdbcTemplate.execute("drop table financial_transaction");
    }

    @Test
    public void equalsValueSrc() {
        int src = jdbcTemplate
                .queryForObject("SELECT SRC FROM financial_transaction WHERE amount = 20", Integer.class);
        assertEquals(1, src);
    }

    @Test
    public void equalsValueDst() {
        int dst = jdbcTemplate
                .queryForObject("SELECT DST FROM financial_transaction WHERE amount = 20", Integer.class);
        assertEquals(10, dst);
    }

    @Test
    public void equalsValueAmount() {
        double amount = jdbcTemplate
                .queryForObject("SELECT AMOUNT FROM financial_transaction WHERE src = 1", Double.class);
        assertEquals(20, amount, 1e-9);
    }

    @Test
    public void countRowAfterInsert() {
        int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM financial_transaction", Integer.class);
        assertEquals(2, count);
    }
}
