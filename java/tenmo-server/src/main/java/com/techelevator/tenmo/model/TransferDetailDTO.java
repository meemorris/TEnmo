package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class TransferDetailDTO {

    private long transferId;
    private String transferTypeDesc;
    private String transferStatusDesc;
    private BigDecimal amount;
    private long fromAcct;
    private long fromUserId;
    private String fromUsername;
    private BigDecimal currentToBalance;
    private long toAccount;
    private long toUserId;
    private String toUsername;
    private String displayFromOrTo;


    public long getTransferId() {
        return transferId;
    }

    public void setTransferId(long transferId) {
        this.transferId = transferId;
    }

    public String getTransferTypeDesc() {
        return transferTypeDesc;
    }

    public void setTransferTypeDesc(String transferTypeDesc) {
        this.transferTypeDesc = transferTypeDesc;
    }

    public String getTransferStatusDesc() {
        return transferStatusDesc;
    }

    public void setTransferStatusDesc(String transferStatusDesc) {
        this.transferStatusDesc = transferStatusDesc;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public long getFromAcct() {
        return fromAcct;
    }

    public void setFromAcct(long fromAcct) {
        this.fromAcct = fromAcct;
    }

    public long getToAccount() {
        return toAccount;
    }

    public void setToAccount(long toAccount) {
        this.toAccount = toAccount;
    }

    public long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public long getToUserId() {
        return toUserId;
    }

    public void setToUserId(long toUserId) {
        this.toUserId = toUserId;
    }

    public String getFromUsername() {
        return fromUsername;
    }

    public void setFromUsername(String fromUsername) {
        this.fromUsername = fromUsername;
    }

    public BigDecimal getCurrentToBalance() {
        return currentToBalance;
    }

    public void setCurrentToBalance(BigDecimal currentToBalance) {
        this.currentToBalance = currentToBalance;
    }

    public String getToUsername() {
        return toUsername;
    }

    public void setToUsername(String toUsername) {
        this.toUsername = toUsername;
    }

    public String getDisplayFromOrTo() {
        return displayFromOrTo;
    }

    public void setDisplayFromOrTo(String displayFromOrTo) {
        this.displayFromOrTo = displayFromOrTo;
    }
}
