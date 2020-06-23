package com.highfi.gpagn.service;

import com.highfi.gpagn.exceptions.EntityValidationException;
import com.highfi.gpagn.exceptions.InvalidChangeRateException;
import com.highfi.gpagn.model.Balance;
import com.highfi.gpagn.model.ChangeRate;
import com.highfi.gpagn.model.Movement;

import java.util.List;

public interface BalanceService {
    Balance calculBalance(Balance lastBalance, List<Movement> movements, List<ChangeRate> changeRates) throws InvalidChangeRateException, EntityValidationException;
}
