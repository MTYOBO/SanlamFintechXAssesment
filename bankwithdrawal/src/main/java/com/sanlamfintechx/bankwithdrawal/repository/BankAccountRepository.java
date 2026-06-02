package com.sanlamfintechx.bankwithdrawal.repository;

import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

import org.springframework.jdbc.core.JdbcTemplate;

@Repository
public class BankAccountRepository {
    private final JdbcTemplate jdbcTemplate;

    public BankAccountRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public BigDecimal getCurrentBalance(Long accountId) {
        String sql = "SELECT balance FROM accounts WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, accountId);
    }

    public int updateBalance(BigDecimal amount, Long accountId) {
        String sql = "UPDATE accounts SET balance = balance - ? WHERE id = ?";
        return jdbcTemplate.update(sql, amount, accountId);
    }

}
