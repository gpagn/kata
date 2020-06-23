package com.highfi.gpagn.service;

import com.highfi.gpagn.exceptions.EntityValidationException;
import com.highfi.gpagn.exceptions.InvalidChangeRateException;
import com.highfi.gpagn.model.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class StatementServiceImpl implements StatementService {

    private MovementService movementService;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("DD/MM/YYYY");

    public StatementServiceImpl(MovementService movementService) {
        this.movementService = movementService;
    }

    @Override
    public Statement generateStatement(Date startDate, Date endDate, Balance balance, List<Movement> movements, List<ChangeRate> changeRates) throws InvalidChangeRateException, EntityValidationException {
        Statement statement = new Statement();
        statement.setBankAccount(balance.getBankAccount());
        statement.setEndDate(endDate);
        statement.setStartDate(startDate);
        if (movements != null && !movements.isEmpty()) {
            double balanceAmount = balance.getInitialBalance();
            for (Movement movement : movements) {
                double movementAmount = movementService.calculateMovementAmount(movement, changeRates);
                balanceAmount += movementAmount;
                statement.addStatementLine(StatementLine.builder()
                        .operation(movement.getMovementType().toString())
                        .tradeDate(movement.getTradeDate())
                        .amount(movementAmount)
                        .balance(balanceAmount)
                        .build());
            }
        }
        return statement;
    }

    @Override
    public String printStatement(Statement statement) {
        StringBuilder builder = new StringBuilder();
        builder.append(statement.getHeader()).append(System.lineSeparator());
        for (StatementLine line : statement.getSortedStatementLines()) {
            builder.append(printStatementLine(line, " | ")).append(System.lineSeparator());
        }
        return builder.toString();
    }


    private String printStatementLine(StatementLine statementLine, String separator) {
        StringBuilder builder = new StringBuilder();

        return builder.append(statementLine.getOperation()).append(separator)
                .append(dateFormat.format(statementLine.getTradeDate())).append(separator)
                .append(String.format("%.2f", statementLine.getAmount())).append(separator)
                .append(String.format("%.2f", statementLine.getBalance())).toString();

    }
}
