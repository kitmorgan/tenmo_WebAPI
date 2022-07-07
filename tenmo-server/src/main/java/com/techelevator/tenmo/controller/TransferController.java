package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.security.jwt.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    private UserDao userDao;

    @Autowired
    private TransferDao transferDao;

    public TransferController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, TransferDao transferDao){
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.transferDao = transferDao;
    }
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/transfer/send", method = RequestMethod.POST)
    public void sendMoney(@RequestBody Transfer moneyTransfer, Principal principal) throws Exception {
        Boolean success = false;
        success = transferDao.sendTransfer(principal.getName(), moneyTransfer.getToUsername(), moneyTransfer.getTransferAmount());
        if (!success){
            throw new Exception("Transfer Failed!");
        }
    }

}
