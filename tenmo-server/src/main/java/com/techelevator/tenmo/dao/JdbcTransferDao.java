package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.UsernameNotFoundException;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.SQLWarningException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String updateTransfer(Transfer transfer, int statusId) {
        return null;
    }

    @Override
    public String transferRequest(String fromUsername, String toUsername, BigDecimal total) {
        return null;
    }

    @Override
    public boolean sendTransfer(String fromUsername, String toUsername, BigDecimal transferAmount) {
        if (checkBalance(fromUsername, transferAmount)) {
            String sql = "UPDATE account set balance = account.balance - ? FROM tenmo_user where tenmo_user.user_id = account.user_id AND username = ?;" +
                    " UPDATE account set balance = account.balance + ? FROM tenmo_user where tenmo_user.user_id = account.user_id AND username = ?;" +
                    " INSERT INTO transfer (toUsername, fromUsername, status, transfer_amount) VALUES (?, ?, 'APPROVED', ?);";

            jdbcTemplate.update(sql, transferAmount, fromUsername, transferAmount, toUsername, toUsername, fromUsername, transferAmount);
            return true;
        }
        return false;
    }

    public boolean checkBalance(String fromUsername, BigDecimal transferAmount) {
        String sql = "SELECT balance FROM account JOIN tenmo_user as tu ON tu.user_id = account.user_id where username = ?";
        BigDecimal balance;
        try {
            balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, fromUsername);
        } catch (DataAccessException e) {
            balance = null;
        }
        return balance.compareTo(transferAmount) != -1;
    }

    @Override
    public List<Transfer> pendingRequests(int userId) {
        return null;
    }

    @Override
    public List<Transfer> allTransfers(String username) {
        try {
            String sql = "select transfer_id, tousername, fromusername, status, transfer_amount from transfer WHERE tousername = ? OR fromusername = ?;";
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, username, username);
            List<Transfer> transfers = new ArrayList<>();
            while (result.next()) {
                Transfer transfer = new Transfer();
                transfer.setTransferId(result.getInt("transfer_id"));
                transfer.setToUsername(result.getString("tousername"));
                transfer.setFromUsername(result.getString("fromusername"));
                transfer.setStatus(result.getString("status"));
                transfer.setTransferAmount(result.getBigDecimal("transfer_amount"));
                transfers.add(transfer);
            }
            return transfers;
        }catch (DataAccessException e){
            System.out.println(e);
        }
        return null;
    }
//

    @Override
    public Transfer getTransferId(int transactionId) {
        return null;
    }
}
