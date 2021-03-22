package com.techelevator.tenmo.dao;


import java.math.BigDecimal;


public interface AccountDAO {

    BigDecimal getBalance(long id);

    Long getUserIdByAccount(Long accountId);
}
