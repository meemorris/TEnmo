package com.techelevator.tenmo.model;

public class TransferStatusDTO {

    private long transferId;
    private String transferStatusDesc;

    public long getTransferId() {
        return transferId;
    }

    public void setTransferId(long transferId) {
        this.transferId = transferId;
    }

    public void setTransferStatusDesc(String transferStatusDesc) {
        this.transferStatusDesc = transferStatusDesc;
    }

    public String getTransferStatusDesc() {
        return transferStatusDesc;
    }



}
