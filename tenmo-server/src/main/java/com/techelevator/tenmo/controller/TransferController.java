package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exception.TransactionNotFoundException;
import com.techelevator.tenmo.model.Request;
import com.techelevator.tenmo.model.Respond;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.security.jwt.TokenProvider;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.ParsedSql;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    private UserDao userDao;

    @Autowired
    private TransferDao transferDao;

    public TransferController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, TransferDao transferDao) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.transferDao = transferDao;
    }

    @ApiOperation("Returns a list of all transfers to and from the user")
    @RequestMapping(path = "/transfers", method = RequestMethod.GET)
    public List<Transfer> allTransfers(Principal principal) {
        try {
            List<Transfer> transfers = transferDao.allTransfers(principal.getName());

            return transfers;
        }catch (DataRetrievalFailureException d){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Admin has been notified");
        }
    }

    @ApiOperation("Send TE bucks to a specified user ('toUsername'), 'transferAmount' must be a positive value to a valid other user")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/transfers/send/", method = RequestMethod.POST)
    public void sendMoney(@RequestBody Transfer moneyTransfer, Principal principal) {
        if (principal.getName().equals(moneyTransfer.getToUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You just tried to send money to yourself! You don't need us to do that.");
        }
        Boolean success = false;
        try {
            success = transferDao.sendTransfer(principal.getName(), moneyTransfer.getToUsername(), moneyTransfer.getTransferAmount());
            if (!success) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Transfer Failed!");
            }
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User " + moneyTransfer.getToUsername() + " does not exist.");
        }
    }

    @ApiOperation("Get your transfers by id")
    @RequestMapping(path = "transfers/{transferId}", method = RequestMethod.GET)
    public Transfer getTransfersById(@PathVariable int transferId, Principal principal){
        try {
            Transfer transfer = transferDao.getTransferById(transferId);
            if (principal.getName().equals(transfer.getToUsername()) || principal.getName().equals(transfer.getFromUsername())){
                return transfer;
            }else{
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Nunya");
            }
        } catch (Exception e){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiOperation("Request money")
    @RequestMapping(path = "/transfers/requests", method = RequestMethod.POST)
    public void requestMoney(@RequestBody @Valid Request request, Principal principal){
        boolean success = transferDao.transferRequest(request, principal.getName());
        if(!success) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request failed.");
        }
    }

    @ApiOperation("Get a list of pending transfers")
    @RequestMapping(path = "/transfers/requests", method = RequestMethod.GET)
    public List<Transfer> getRequests(Principal principal){
        try {
            List<Transfer> transfers = transferDao.pendingRequests(principal.getName());
            return transfers;
        }catch (DataRetrievalFailureException d){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Admin has been notified");
        }
    }

    @ApiOperation("Approve or reject a request")
    @RequestMapping(path = "/transfers/requests", method = RequestMethod.PUT)
    public void respondToRequest(Respond respond, Principal principal){
        if(respond.getAccept()){
            transferDao.acceptRequest(respond, principal.getName());
        }else{
            transferDao.rejectRequest();
            //TODO: this should remove the request from transfer table in sql
            throw new ResponseStatusException(HttpStatus.OK, "Request has been rejected");
        }
        //TODO: approve or reject
    }

}
