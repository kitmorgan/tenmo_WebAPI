package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    public String updateTransfer (Transfer transfer, int statusId);

    public String transferRequest (int fromUser, int toUser, BigDecimal total);

    public String transferSent (int fromUser, int toUser, BigDecimal total);

    public List<Transfer> pendingRequests (int userId);

    public List<Transfer> allTransfers (int userId);

    public Transfer getTransferId (int transactionId);
}
