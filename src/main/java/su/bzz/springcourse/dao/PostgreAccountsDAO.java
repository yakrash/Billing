package su.bzz.springcourse.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import su.bzz.springcourse.model.Account;
import su.bzz.springcourse.model.FinancialTransaction;

import java.util.List;

@Repository
public class PostgreAccountsDAO implements AccountsDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PostgreAccountsDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Account> rowMapper = (rs, i) -> {
        Account account = new Account();
        account.setId(rs.getLong("id"));
        account.setDebit(rs.getDouble("debit"));
        account.setCredit(rs.getDouble("credit"));
        return account;
    };

    @Override
    public long create(double debit, double credit) {
        String sql = "INSERT INTO accounts(debit, credit) values(?, ?) RETURNING id";
        return jdbcTemplate.queryForObject(sql, Long.class, debit, credit);
    }

    @Transactional
    @Override
    public Account get(long id) {
        String sql = "SELECT * FROM ACCOUNTS WHERE ID=?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    @Override
    @Transactional
    public void modify(List<FinancialTransaction> financialTransactionsList) {
        String sql = "UPDATE ACCOUNTS SET debit=?, credit=? WHERE id=?";

        for (FinancialTransaction e : financialTransactionsList) {
            Account accountDstFromDB = get(e.getDst());
            Account accountSrcFromDB = get(e.getSrc());

            accountSrcFromDB.setDebit(accountSrcFromDB.getDebit() + e.getAmount());
            accountDstFromDB.setCredit(accountDstFromDB.getCredit() + e.getAmount());

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
