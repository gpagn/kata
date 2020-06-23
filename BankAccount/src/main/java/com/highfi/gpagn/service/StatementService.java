package com.highfi.gpagn.service;

import com.highfi.gpagn.exceptions.EntityValidationException;
import com.highfi.gpagn.exceptions.InvalidChangeRateException;
import com.highfi.gpagn.model.Balance;
import com.highfi.gpagn.model.ChangeRate;
import com.highfi.gpagn.model.Movement;
import com.highfi.gpagn.model.Statement;

import java.util.Date;
import java.util.List;

public interface StatementService {

    Statement generateStatement(Date startDate, Date endDate, Balance balance, List<Movement> movements, List<ChangeRate> changeRates) throws InvalidChangeRateException, EntityValidationException;

    String printStatement(Statement statement);
}
