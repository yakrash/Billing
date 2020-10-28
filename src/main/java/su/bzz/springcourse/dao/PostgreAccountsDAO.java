package su.bzz.springcourse.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import su.bzz.springcourse.model.Account;
import su.bzz.springcourse.model.FinancialTransaction;
import su.bzz.springcourse.service.TransactionLogger;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class PostgreAccountsDAO implements AccountsDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionLogger.class);
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Account> rowMapper = (rs, i) -> {
        Account account = new Account();
        account.setId(rs.getLong("id"));
        account.setDebit(rs.getDouble("debit"));
        account.setCredit(rs.getDouble("credit"));
        return account;
    };

    @Autowired
    public PostgreAccountsDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long create(double debit, double credit) {
        String sql = "INSERT INTO accounts(debit, credit) values(?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sql, new String[]{"id"});
            ps.setDouble(1, debit);
            ps.setDouble(2, credit);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    @Transactional
    @Override
    public Account get(long id) {
        try {
            String sql = "SELECT * FROM ACCOUNTS WHERE ID=?";
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.warn("Account not found, error: " + e.toString());
            return null;
        }

    }

    @Override
    @Transactional
    public void modify(List<FinancialTransaction> financialTransactionsList) {
        String sql = "UPDATE ACCOUNTS SET debit=?, credit=? WHERE id=?";

        for (FinancialTransaction e : financialTransactionsList) {
            Account accountDstFromDB = get(e.getDst());
            Account accountSrcFromDB = get(e.getSrc());

            accountSrcFromDB.setCredit(accountSrcFromDB.getCredit() + e.getAmount());
            accountDstFromDB.setDebit(accountDstFromDB.getDebit() + e.getAmount());

            jdbcTemplate.update(sql, (ps) -> {
                int i = 0;
                ps.setDouble(++i, accountDstFromDB.getDebit());
                ps.setDouble(++i, accountDstFromDB.getCredit());
                ps.setLong(++i, accountDstFromDB.getId());
            });

            jdbcTemplate.update(sql, (ps) -> {
                int i = 0;
                ps.setDouble(++i, accountSrcFromDB.getDebit());
                ps.setDouble(++i, accountSrcFromDB.getCredit());
                ps.setLong(++i, accountSrcFromDB.getId());
            });
        }
    }
}
