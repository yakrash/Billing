package su.bzz.springcourse.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import su.bzz.springcourse.model.Account;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class PostgreAccountsDAO implements AccountsDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PostgreAccountsDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<Account> rowMapper = new RowMapper<Account>() {
        @Override
        public Account mapRow(ResultSet rs, int i) throws SQLException {
            Account account = new Account();
            account.setId(rs.getLong("id"));
            account.setDebit(rs.getDouble("debit"));
            account.setCredit(rs.getDouble("credit"));
            return account;
        }
    };

    @Override
    public long create(double debit, double credit) {
        String sql = "INSERT INTO accounts(debit, credit) values(?, ?) RETURNING id";
        return jdbcTemplate.queryForObject(sql, Long.class, debit, credit);
    }

    @Override
    public Account get(long id) {
        String sql = "SELECT * FROM ACCOUNTS WHERE ID=?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

}
