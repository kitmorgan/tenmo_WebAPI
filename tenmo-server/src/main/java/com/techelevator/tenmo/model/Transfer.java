package com.techelevator.tenmo.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Component
public class Transfer {
    private int transferId;
    private int toAccountId;
    private int fromAccountId;
    private BigDecimal transferAmount;
    private String status;

    //TODO: add constructor, getter, setter
}
