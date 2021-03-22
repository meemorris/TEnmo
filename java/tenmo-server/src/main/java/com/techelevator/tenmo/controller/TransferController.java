package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RequestMapping("/account")
@RestController
//starting path: http://localhost:8080
public class TransferController {

    private AccountDAO accountDAO;
    private UserDAO userDAO;
    private TransferDAO transferDAO;

    public TransferController(AccountDAO accountDAO, UserDAO userDAO, TransferDAO transferDAO) {
        this.accountDAO = accountDAO;
        this.userDAO = userDAO;
        this.transferDAO = transferDAO;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/transfer", method = RequestMethod.POST)
    public TransferStatusDTO send(Principal principal, @Valid @RequestBody TransferRequestDTO transfer) {
        User currentUser = userDAO.findByUsername(principal.getName());
        long option = 3;
        return transferDAO.sendBucks(transfer, currentUser, option);
    }

    @RequestMapping(path = "/transfer/history", method = RequestMethod.GET)
    public List<TransferDetailDTO> viewTransfers(Principal principal) {
        User currentUser = userDAO.findByUsername(principal.getName());
        return transferDAO.getTransferList(currentUser);
    }

    @RequestMapping(path = "/transfer/history/{transferID}", method = RequestMethod.GET)
    public TransferDetailDTO getTransfer(Principal principal, @PathVariable Long transferID) {
        User currentUser = userDAO.findByUsername(principal.getName());
        return transferDAO.getTransfer(currentUser, transferID);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/transfer/request/{requestedUserId}", method = RequestMethod.POST)
    public TransferStatusDTO requestTransfer(Principal principal, @PathVariable Long requestedUserId,
                                             @Valid @RequestBody TransferRequestDTO transfer) {
        User currentUser = userDAO.findByUsername(principal.getName());
        return transferDAO.requestTransfer(currentUser, requestedUserId, transfer);
    }

    @RequestMapping(path = "/transfer/requests", method = RequestMethod.GET)
    public List<TransferRequestDTO> viewPendingRequests(Principal principal) {
        User currentUser = userDAO.findByUsername(principal.getName());
        return transferDAO.listPendingRequests(currentUser);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/transfer/requests/{option}", method = RequestMethod.POST)
    public TransferStatusDTO executeTransfer(Principal principal, @Valid @RequestBody TransferRequestDTO transfer,
                                             @PathVariable Long option) {
        User currentUser = userDAO.findByUsername(principal.getName());
        return transferDAO.sendBucks(transfer, currentUser, option);
    }








}
