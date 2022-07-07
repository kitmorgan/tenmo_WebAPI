package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

        private JdbcTemplate jdbcTemplate;

        @Override
        public String updateTransfer(Transfer transfer, int statusId) {
                return null;
        }

        @Override
        public String transferRequest(int fromUserId, int toUserId, BigDecimal total) {
                return null;
        }

        @Override
        public boolean sendTransfer (int fromUserId, int toUserId, BigDecimal transferAmount) {
                if (checkBalance(fromUserId, transferAmount)) {
                        String sql = "UPDATE account set balance = balance - ? where user_id = ?;" +
                                " UPDATE account set balance = balance + ? where user_id = ?;" + "INSERT INTO transfer (toUser_id, fromUser_id, status, transfer_amount) VALUES (1001, 1002, 'APPROVED', 20);";
                        jdbcTemplate.update(sql, fromUserId, toUserId);
                        return true;
                }
                return false;
        }

        public boolean checkBalance(int fromUserId, BigDecimal transferAmount){
                String sql = "SELECT balance FROM account where user_id = ?";
                BigDecimal balance;
                try{
                        balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, fromUserId);
                }catch (DataAccessException e){
                        balance = null;
                }
                return balance.compareTo(transferAmount) != -1;
        }

        @Override
        public List<Transfer> pendingRequests(int userId) {
                return null;
        }

        @Override
        public List<Transfer> allTransfers(int userId) {
                return null;
        }

        @Override
        public Transfer getTransferId(int transactionId) {
                return null;
        }
}
