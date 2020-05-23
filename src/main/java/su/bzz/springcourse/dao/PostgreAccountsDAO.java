package su.bzz.springcourse.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import su.bzz.springcourse.model.Account;

@Repository
public class PostgreAccountsDAO implements AccountsDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PostgreAccountsDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long create(double amount) {
        String sql = "INSERT INTO accounts(amount) values(?) RETURNING id";
        return jdbcTemplate.queryForObject(sql, Long.class, amount);
    }

    @Override
    public Account get(long id) {
        String sql = "SELECT AMOUNT FROM ACCOUNTS WHERE ID=?";

        double amount = jdbcTemplate.queryForObject(sql, Double.class, id);
        return new Account(id, amount);
    }
}
