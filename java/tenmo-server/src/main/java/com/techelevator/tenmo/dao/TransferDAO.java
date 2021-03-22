package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.*;

import java.util.List;

public interface TransferDAO {

    TransferStatusDTO sendBucks(TransferRequestDTO transfer, User currentUser, long option);

    List<TransferDetailDTO> getTransferList(User currentUser);

    TransferDetailDTO getTransfer(User currentUser, Long transferID);

    TransferStatusDTO requestTransfer(User currentUser, Long requestedUserId, TransferRequestDTO transfer);

    List<TransferRequestDTO> listPendingRequests(User currentUser);

}
