package su.bzz.springcourse.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import su.bzz.springcourse.model.Account;

@Repository
public class AccountsDAOImp implements AccountsDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AccountsDAOImp(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(double amount) {
        String sql = "INSERT INTO accounts(amount) values(?)";
        jdbcTemplate.update(sql, amount);

    }

    @Override
    public Account get(int id) {
        try {
            String sql = "SELECT AMOUNT FROM ACCOUNTS WHERE ID=?";

            double amount = jdbcTemplate.queryForObject(sql, Double.class, id);
            return new Account(id, amount);
        }
        catch(EmptyResultDataAccessException e){
            return null;
        }
    }
}
