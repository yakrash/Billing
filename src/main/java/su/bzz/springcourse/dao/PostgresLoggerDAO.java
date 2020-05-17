package su.bzz.springcourse.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import su.bzz.springcourse.model.FinancialTransaction;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class PostgresLoggerDAO implements FinancialTransactionDAO {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    @Transactional
    public void insert(List<FinancialTransaction> financialTransactionList) {
        String sql = "INSERT INTO financial_transaction(src, dst, amount) values(?, ?, ?)";

        for (FinancialTransaction e : financialTransactionList) {
            jdbcTemplate.update(sql, e.getSrc(), e.getDst(), e.getAmount());
        }

    }
}
