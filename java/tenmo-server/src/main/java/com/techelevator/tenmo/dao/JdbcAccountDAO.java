package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.security.Principal;

@Component
public class JdbcAccountDAO implements AccountDAO {

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BigDecimal getBalance(long id) {
        String sql = "SELECT SUM(balance) AS balance FROM accounts WHERE user_id = ?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        BigDecimal balance = null;
        while(results.next()) {
           balance = results.getBigDecimal("balance");
        }
        return balance;
    }

    @Override
    public Long getUserIdByAccount(Long accountId) {
        Account account = new Account();

        String sql = "SELECT account_id, user_id, balance FROM accounts WHERE " +
                "account_id = ?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);

        if (results.next()) {
            account = mapRowToAccount(results);
        }
        return account.getUserID();
    }


    private Account mapRowToAccount(SqlRowSet results) {
        Account account = new Account();
        account.setAccountID(results.getLong("account_id"));
        account.setUserID(results.getLong("user_id"));
        account.setBalance(results.getBigDecimal("balance"));
        return account;
    }

}
