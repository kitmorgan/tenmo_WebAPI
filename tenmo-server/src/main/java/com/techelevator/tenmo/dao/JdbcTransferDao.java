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
        public boolean sendTransfer (String fromUsername, String toUsername, BigDecimal transferAmount) {
                if (checkBalance(fromUsername, transferAmount)) {
                        String sql = "UPDATE account SET balance = account.balance - ? FROM account as ac JOIN tenmo_user as tu ON ac.user_id = tu.user_id WHERE username = '?';" +
                                " UPDATE account SET balance = account.balance + ? FROM account as ac JOIN tenmo_user as tu ON ac.user_id = tu.user_id WHERE username = '?';";
                        jdbcTemplate.update(sql, transferAmount, fromUsername, transferAmount, toUsername);
                        jdbcTemplate.update(" INSERT INTO transfer (toUsername, fromUsername, status, transfer_amount) VALUES (?, ?, 'APPROVED', ?);", toUsername, fromUsername, transferAmount);
                        return true;
                }
                return false;
        }

        public boolean checkBalance(String fromUsername, BigDecimal transferAmount){
                String sql = "SELECT balance FROM account JOIN tenmo_user as tu ON tu.user_id = account.user_id where username = ?";
                BigDecimal balance;
                try{
                        balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, fromUsername);
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
