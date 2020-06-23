package com.highfi.gpagn.service;

import com.highfi.gpagn.exceptions.EntityValidationException;
import com.highfi.gpagn.exceptions.InvalidChangeRateException;
import com.highfi.gpagn.model.BankAccount;
import com.highfi.gpagn.model.ChangeRate;
import com.highfi.gpagn.model.Currency;
import com.highfi.gpagn.model.Movement;

import java.util.List;

public interface MovementService {

    Movement createDepositMovement(BankAccount bankAccount, double amount, Currency currency) throws EntityValidationException;

    Movement createWithdrawalMovement(BankAccount bankAccount, double amount, Currency currency) throws EntityValidationException;

    double calculateMovementAmount(Movement movement, List<ChangeRate> changeRates) throws EntityValidationException, InvalidChangeRateException;
}
