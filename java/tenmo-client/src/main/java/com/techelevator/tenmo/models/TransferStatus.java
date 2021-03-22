package com.techelevator.tenmo.models;

public class TransferStatus {

    private Long transferId;
    private Long transferStatusId;
    private String transferStatusDesc;

    public Long getTransferId() {
        return transferId;
    }

    public void setTransferId(Long transferId) {
        this.transferId = transferId;
    }

    public Long getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(Long transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public void setTransferStatusDesc(String transferStatusDesc) {
        this.transferStatusDesc = transferStatusDesc;
    }

    public String getTransferStatusDesc() {
        return transferStatusDesc;
    }


}
