package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.TransactionNotFoundException;
import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    public String updateTransfer (Transfer transfer, int statusId);

    public String transferRequest (String fromUsername, String toUsername, BigDecimal transferAmount);

    public boolean sendTransfer (String fromUsername, String toUsername, BigDecimal transferAmount);

    public List<Transfer> pendingRequests (int userId);

    public List<Transfer> allTransfers (String username);

    public Transfer getTransferById (int transferId) throws TransactionNotFoundException;
}
