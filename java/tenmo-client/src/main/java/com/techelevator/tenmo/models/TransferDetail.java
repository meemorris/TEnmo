package com.techelevator.tenmo.models;

import java.math.BigDecimal;

public class TransferDetail {

    private Long transferId;
    private String fromUsername;
    private String toUsername;
    private String transferTypeDesc;
    private String transferStatusDesc;
    private BigDecimal amount;
    private String displayFromOrTo;

    public Long getTransferId() {
        return transferId;
    }

    public void setTransferId(Long transferId) {
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

    public String getDisplayFromOrTo() {
        return displayFromOrTo;
    }

    public void setDisplayFromOrTo(String displayFromOrTo) {
        this.displayFromOrTo = displayFromOrTo;
    }



}
