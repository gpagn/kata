package com.highfi.gpagn.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
@Builder
public class StatementLine {

    private Date tradeDate;
    private double amount;
    private String operation;
    private double balance;

}
