package com.techelevator.tenmo.models;

import java.math.BigDecimal;

public class TransferRequest {

    private Long transferId;
    private Long fromUserId;
    private Long toUserId;
    private String fromUsername;
    private String toUsername;
    private BigDecimal transferAmount;
    private String transferType;

    public TransferRequest() {
    }

    public TransferRequest(Long fromUserId, Long toUserId, BigDecimal amount, String transferType) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.transferAmount = amount;
        this.transferType = transferType;
    }

    public void setTransferId(Long transferId) {
        this.transferId = transferId;
    }

    public Long getTransferId() {
        return transferId;
    }

    public Long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    public String getFromUsername() {
        return fromUsername;
    }

    public void setFromUsername(String fromUsername) {
        this.fromUsername = fromUsername;
    }

    public String getToUsername() {
        return toUsername;
    }

    public void setToUsername(String toUsername) {
        this.toUsername = toUsername;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

}
