package com.techelevator.tenmo.models;

import java.math.BigDecimal;

public class Account {

    private long accountId;
    private int userId;
    private BigDecimal balance;

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Account() {

    }

    public Account(long accountID, int userID, BigDecimal balance) {
        this.accountId = accountID;
        this.userId = userID;
        this.balance = balance;
    }



}
