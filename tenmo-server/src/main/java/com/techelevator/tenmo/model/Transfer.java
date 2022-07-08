package com.techelevator.tenmo.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


public class Transfer {
    private int transferId;
    private String toUsername;
    private String fromUsername;
    private BigDecimal transferAmount;
    private String status;
    private String timestamp;

    public Transfer(String toUsername, String fromUsername, BigDecimal transferAmount){
        this.toUsername = toUsername;
        this.fromUsername = fromUsername;
        this.transferAmount = transferAmount;
        this.status = "APPROVED";
    }

    public Transfer(){

    }

    public Transfer(int toUserId, int fromUserId, BigDecimal transferAmount, String status){
        this.toUsername = toUsername;
        this.fromUsername = fromUsername;
        this.transferAmount = transferAmount;
        this.status = "PENDING";
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public String getToUsername() {
        return toUsername;
    }

    public void setToUsername(String toUsername) {
        this.toUsername = toUsername;
    }

    public String getFromUsername() {
        return fromUsername;
    }

    public void setFromUsername(String fromUsername) {
        this.fromUsername = fromUsername;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
