package com.highfi.gpagn.service;

import com.highfi.gpagn.exceptions.EntityValidationException;
import com.highfi.gpagn.exceptions.InvalidChangeRateException;
import com.highfi.gpagn.model.Balance;
import com.highfi.gpagn.model.ChangeRate;
import com.highfi.gpagn.model.Movement;

import java.util.Calendar;
import java.util.List;

public class BalanceServiceImpl implements BalanceService {

    private MovementService movementService;

    public BalanceServiceImpl(MovementService movementService) {
        this.movementService = movementService;
    }

    @Override
    public Balance calculBalance(Balance lastBalance, List<Movement> movements, List<ChangeRate> changeRates) throws InvalidChangeRateException, EntityValidationException {
        double amountMovement = 0.0;
        for (Movement movement : movements) {
            amountMovement += movementService.calculateMovementAmount(movement, changeRates);
        }

        double amountBalance = lastBalance.getInitialBalance() + amountMovement;
        return Balance.builder()
                .bankAccount(lastBalance.getBankAccount())
                .dateBalance(Calendar.getInstance().getTime())
                .initialBalance(amountBalance)
                .build();

    }

}
