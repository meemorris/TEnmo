package com.techelevator.tenmo.models;

import java.math.BigDecimal;

public class Transfer {

    private Long toUserId;
    private BigDecimal transferAmount;

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

}
