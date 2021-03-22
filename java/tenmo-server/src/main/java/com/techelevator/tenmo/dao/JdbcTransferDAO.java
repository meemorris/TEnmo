package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.InsufficientFundsException;
import com.techelevator.tenmo.model.*;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDAO implements TransferDAO {

    private final JdbcTemplate jdbcTemplate;
    private final AccountDAO accountDAO;
    private static final int APPROVED = 2;
    private static final int SEND = 2;
    private static final int REQUEST = 1;
    private static final int PENDING = 1;
    private static final int REJECTED = 3;


    private static final String SELECT_TRANSFER = "SELECT t.transfer_id, tt.transfer_type_desc, ts.transfer_status_desc, t.amount, " +
            "acctFrom.account_id AS from_Acct, acctFrom.user_id AS from_User, acctTo.balance AS current_To_Bal, " +
            "acctTo.account_id AS to_Acct, acctTo.user_id AS to_User, initcap(userFrom.username) AS from_Username, " +
            "initcap(userTo.username) AS to_Username " +
            "FROM transfers t " +
            "JOIN transfer_types tt ON t.transfer_type_id = tt.transfer_type_id " +
            "JOIN transfer_statuses ts ON t.transfer_status_id = ts.transfer_status_id " +
            "JOIN accounts acctFrom ON account_from = acctFrom.account_id " +
            "JOIN accounts acctTo ON account_to = acctTo.account_id " +
            "JOIN users userFrom ON userFrom.user_id = acctFrom.user_id " +
            "JOIN users userTo ON userTo.user_id = acctTo.user_id ";

    private static final String UPDATE_TRANSFERS = "INSERT INTO transfers(transfer_type_id, transfer_status_id, " +
            "account_from, account_to, amount) VALUES (?, ?, ?, ?, ?) RETURNING transfer_id";

    public JdbcTransferDAO(JdbcTemplate jdbcTemplate, AccountDAO accountDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.accountDAO = accountDAO;
    }

    @Override
    public TransferStatusDTO sendBucks(TransferRequestDTO transfer, User currentUser, long option) {
        BigDecimal amount = transfer.getTransferAmount();
        long toUserId = transfer.getToUserId();
        Long fromUserId = currentUser.getId();
        BigDecimal senderBalance = accountDAO.getBalance(fromUserId);
        TransferStatusDTO transferStatus = new TransferStatusDTO();

        try {
            if (option < 3) {
                transferStatus = updateTransfer(transfer, fromUserId, option, currentUser);
            } else if (amount.compareTo(senderBalance) > 0) {
                throw new InsufficientFundsException("Insufficient funds.");
            } else {
                    long transferID = createTransfer(fromUserId, amount, toUserId);
                    updateBalance(fromUserId, amount, currentUser);
                    updateBalance(toUserId, amount, currentUser);
                    transferStatus = getTransferStatus(transferID);
                }
        } catch (InsufficientFundsException| DataAccessException e) {
                transferStatus.setTransferStatusDesc("Rejected");
        }
            return transferStatus;
    }

    public TransferDetailDTO getTransfer(User currentUser, Long transferID) {
        TransferDetailDTO transfer = new TransferDetailDTO();
        String sql = SELECT_TRANSFER +
                "WHERE (account_from IN (SELECT account_id FROM accounts WHERE user_id = ?) OR account_to IN " +
                "(SELECT account_id FROM accounts WHERE user_id = ?)) AND t.transfer_id = ?";

        long currentUserId = currentUser.getId();

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, currentUserId, currentUserId, transferID);

        while(results.next()) {
            transfer = mapRowToTransferDetail(results);
        }
        updateFromTo(currentUser, transfer);
        return transfer;
    }

    public List<TransferDetailDTO> getTransferList(User currentUser) {
        List<TransferDetailDTO> transferList = new ArrayList<>();
        String approved = "Approved";
        String sql = SELECT_TRANSFER +
                "WHERE (account_from IN (SELECT account_id FROM accounts WHERE user_id = ?) OR account_to IN " +
                "(SELECT account_id FROM accounts WHERE user_id = ?)) AND ts.transfer_status_desc = ?";

        long fromUserId = currentUser.getId();

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, fromUserId, fromUserId, approved);

        while(results.next()) {
            transferList.add(mapRowToTransferDetail(results));
        }

        for(TransferDetailDTO item : transferList) {
            updateFromTo(currentUser, item);
        }

        return transferList;
    }

    private void updateFromTo(User currentUser, TransferDetailDTO transfer) {
        if (currentUser.getUsername().equalsIgnoreCase(transfer.getFromUsername())) {
            transfer.setDisplayFromOrTo("To: " + transfer.getToUsername());
        } else {
            transfer.setDisplayFromOrTo("From: " + transfer.getFromUsername());
        }
    }

    @Override
    public TransferStatusDTO requestTransfer(User currentUser, Long requestedUserId, TransferRequestDTO transfer) {
        Long currentUserId = currentUser.getId();
        Long transferId = jdbcTemplate.queryForObject(UPDATE_TRANSFERS, Long.class, PENDING, REQUEST, getUserAccountID(requestedUserId), getUserAccountID(currentUserId), transfer.getTransferAmount());
        return getTransferStatus(transferId);
    }

    @Override
    public List<TransferRequestDTO> listPendingRequests(User currentUser) {
        List<TransferRequestDTO> requests = new ArrayList<>();
        String sql = "SELECT t.transfer_id, ts.transfer_status_desc, t.amount, " +
                "acctFrom.user_id AS from_User, acctTo.user_id AS to_User, " +
                "initcap(userFrom.username) AS from_Username, " +
                "initcap(userTo.username) AS to_Username " +
                "FROM transfers t " +
                "JOIN transfer_statuses ts ON t.transfer_status_id = ts.transfer_status_id " +
                "JOIN accounts acctFrom ON account_from = acctFrom.account_id " +
                "JOIN accounts acctTo ON account_to = acctTo.account_id " +
                "JOIN users userFrom ON userFrom.user_id = acctFrom.user_id " +
                "JOIN users userTo ON userTo.user_id = acctTo.user_id " +
                "WHERE t.account_from IN (SELECT account_id FROM accounts WHERE " +
                "user_id = ?) AND t.transfer_status_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, currentUser.getId(), PENDING);

        while(results.next()) {
            requests.add(mapRowToTransferRequestDTO(results));
        }

        return requests;
    }

    private TransferStatusDTO getTransferStatus(Long transferId) {
        TransferStatusDTO transferStatus = new TransferStatusDTO();
        String sql = "SELECT transfers.transfer_id, ts.transfer_status_desc " +
                "FROM transfer_statuses ts " +
                "JOIN transfers ON transfers.transfer_status_id = ts.transfer_status_id " +
                "WHERE transfer_id = ?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);

        while(results.next()) {
            transferStatus = mapRowToTransferStatus(results);
        }
        return transferStatus;
    }

    private TransferStatusDTO updateTransfer(TransferRequestDTO transfer, long fromUserID, long option, User currentUser) {
        String sql = "UPDATE transfers SET transfer_status_id = ? WHERE transfer_id = ?";
        if (option == 1) {
            jdbcTemplate.update(sql, APPROVED, transfer.getTransferId());
            updateBalance(fromUserID, transfer.getTransferAmount(), currentUser);
            updateBalance(transfer.getToUserId(), transfer.getTransferAmount(), currentUser);
        } else if (option == 2){
            jdbcTemplate.update(sql, REJECTED, transfer.getTransferId());
        }
        return getTransferStatus(transfer.getTransferId());
    }
    private Long createTransfer(long fromUserID, BigDecimal amount, long toUserID) {
        String sql = "INSERT INTO transfers(transfer_type_id, transfer_status_id, account_from, account_to, amount)" +
                "VALUES (?, ?, ?, ?, ?) RETURNING transfer_id";
        return jdbcTemplate.queryForObject(sql, Long.class, APPROVED, SEND, getUserAccountID(fromUserID), getUserAccountID(toUserID), amount);
    }

    //put a subquery (the value for approved and value for send, doesn't depend on other tables

    private Long getUserAccountID(long id) {
        String accountIdSql = "SELECT account_id FROM accounts WHERE user_id = ?";
        return jdbcTemplate.queryForObject(accountIdSql, Long.class, id);
    }

    private void updateBalance(long id, BigDecimal amount, User currentUser) {
        BigDecimal updatedBalance;
        BigDecimal balance = accountDAO.getBalance(id);
        if (id == currentUser.getId()) {
            updatedBalance = balance.subtract(amount);
        } else {
            updatedBalance = balance.add(amount);
        }

        String moreSql = "UPDATE accounts SET balance = ? WHERE user_id = ?";
        jdbcTemplate.update(moreSql, updatedBalance, id);
    }

    private Transfer mapRowToTransfer(SqlRowSet results) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(results.getLong("transfer_id"));
        transfer.setTransferTypeId(results.getLong("transfer_type_id"));
        transfer.setTransferStatusId(results.getLong("transfer_status_id"));
        transfer.setAccountFrom(results.getLong("account_from"));
        transfer.setAccountTo(results.getLong("account_to"));
        transfer.setAmount(results.getBigDecimal("amount"));
        return transfer;
    }

    private TransferDetailDTO mapRowToTransferDetail(SqlRowSet results) {
        TransferDetailDTO transferDetail = new TransferDetailDTO();
        transferDetail.setTransferId(results.getLong("transfer_id"));
        transferDetail.setTransferTypeDesc(results.getString("transfer_type_desc"));
        transferDetail.setTransferStatusDesc(results.getString("transfer_status_desc"));
        transferDetail.setAmount(results.getBigDecimal("amount"));
        transferDetail.setFromAcct(results.getLong("from_acct"));
        transferDetail.setFromUserId(results.getLong("from_user"));
        transferDetail.setCurrentToBalance(results.getBigDecimal("current_to_bal"));
        transferDetail.setToAccount(results.getLong("to_acct"));
        transferDetail.setToUserId(results.getLong("to_user"));
        transferDetail.setFromUsername(results.getString("from_username"));
        transferDetail.setToUsername(results.getString("to_username"));
        return transferDetail;
    }

    private TransferStatusDTO mapRowToTransferStatus(SqlRowSet results) {
        TransferStatusDTO transferStatus = new TransferStatusDTO();
        transferStatus.setTransferId(results.getLong("transfer_id"));
        transferStatus.setTransferStatusDesc(results.getString("transfer_status_desc"));
        return transferStatus;
    }

    private TransferRequestDTO mapRowToTransferRequestDTO(SqlRowSet results) {
        TransferRequestDTO request = new TransferRequestDTO();
        request.setTransferId(results.getLong("transfer_id"));
        request.setFromUserId(results.getLong("from_User"));
        request.setToUserId(results.getLong("to_User"));
        request.setFromUsername(results.getString("from_Username"));
        request.setToUsername(results.getString("to_Username"));
        request.setTransferAmount(results.getBigDecimal("amount"));
        request.setTransferStatus(results.getString("transfer_status_desc"));
        return request;
    }


}
