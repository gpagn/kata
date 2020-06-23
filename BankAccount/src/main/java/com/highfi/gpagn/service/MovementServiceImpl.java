package com.highfi.gpagn.service;

import com.highfi.gpagn.exceptions.EntityValidationException;
import com.highfi.gpagn.exceptions.InvalidChangeRateException;
import com.highfi.gpagn.model.BankAccount;
import com.highfi.gpagn.model.ChangeRate;
import com.highfi.gpagn.model.Currency;
import com.highfi.gpagn.model.Movement;
import com.highfi.gpagn.model.enums.MovementType;
import com.highfi.gpagn.model.enums.Sens;
import com.highfi.gpagn.utils.ValidatorUtils;
import org.apache.commons.lang3.time.DateUtils;

import javax.validation.ConstraintViolation;
import java.util.*;

public class MovementServiceImpl implements MovementService {

    private void validateMovement(Movement movement) throws EntityValidationException {
        Set<ConstraintViolation<Movement>> violations = ValidatorUtils.validator.validate(movement);
        if (!violations.isEmpty()) {
            StringBuilder builder = new StringBuilder("Movement validation fail: ").append(System.lineSeparator());

            violations.stream().forEach(v -> builder
                    .append(v.getPropertyPath().toString())
                    .append(" ")
                    .append(v.getMessage())
                    .append(System.lineSeparator())
            );
            throw new EntityValidationException(builder.toString());

        }
    }

    private void validateChangeRate(Movement movement, ChangeRate changeRate) throws EntityValidationException, InvalidChangeRateException {
        validateMovement(movement);
        Set<ConstraintViolation<ChangeRate>> violations = ValidatorUtils.validator.validate(changeRate);
        if (!violations.isEmpty()) {
            StringBuilder builder = new StringBuilder("ChangeRate validation fail: ").append(System.lineSeparator());

            violations.stream().forEach(v -> builder
                    .append(v.getPropertyPath().toString())
                    .append(" ")
                    .append(v.getMessage())
                    .append(System.lineSeparator())
            );
            throw new EntityValidationException(builder.toString());

        }
        if (!(movement.getReceiverBankAccount().getCurrency().equals(changeRate.getInitialCurrency())
                && movement.getMovementCurrency().equals(changeRate.getConvertedCurrency()))) {
            StringBuilder builder = new StringBuilder("Validate Change Rate fail: need change Rate ")
                    .append(movement.getReceiverBankAccount().getCurrency().getCode())
                    .append("/")
                    .append(movement.getMovementCurrency().getCode())
                    .append("but receive: ")
                    .append(changeRate.toString());
            throw new InvalidChangeRateException(builder.toString());
        }
        if (!DateUtils.isSameDay(movement.getTradeDate(), changeRate.getRateDate())) {
            throw new InvalidChangeRateException("Movement date and change rate date are different");
        }
    }

    @Override
    public Movement createDepositMovement(BankAccount bankAccount, double amount, Currency currency) throws EntityValidationException {
        Date date = Calendar.getInstance().getTime();

        Movement movement = Movement.builder()
                .movementCurrency(currency)
                .movementType(MovementType.DEPOSIT)
                .amount(amount)
                .receiverBankAccount(bankAccount)
                .sens(Sens.CREDIT)
                .tradeDate(date)
                .build();

        validateMovement(movement);

        return movement;
    }

    @Override
    public Movement createWithdrawalMovement(BankAccount bankAccount, double amount, Currency currency) throws EntityValidationException {
        Date date = Calendar.getInstance().getTime();

        Movement movement = Movement.builder()
                .movementCurrency(currency)
                .movementType(MovementType.DEPOSIT)
                .amount(amount)
                .receiverBankAccount(bankAccount)
                .sens(Sens.DEBIT)
                .tradeDate(date)
                .build();

        validateMovement(movement);

        return movement;
    }

    private ChangeRate retreiveChangeRate(Movement movement, List<ChangeRate> changeRates) throws InvalidChangeRateException {
        return changeRates.stream()
                .filter(
                        changeRate -> Objects.equals(changeRate.getInitialCurrency(), movement.getReceiverBankAccount().getCurrency())
                                && Objects.equals(changeRate.getConvertedCurrency(), movement.getMovementCurrency())
                                && DateUtils.isSameDay(changeRate.getRateDate(), movement.getTradeDate()))
                .findFirst()
                .orElseThrow(() -> new InvalidChangeRateException("No change rate found"));
    }

    @Override
    public double calculateMovementAmount(Movement movement, List<ChangeRate> changeRates) throws EntityValidationException, InvalidChangeRateException {
        ChangeRate changeRate = retreiveChangeRate(movement, changeRates);
        validateChangeRate(movement, changeRate);
        double amount = movement.getAmount();
        if (Sens.DEBIT.equals(movement.getSens())) {
            amount *= -1;
        }
        amount *= changeRate.getRate();
        return amount;
    }

//    private void checkChangeRate(Currency bankAccountCurrency, ChangeRate changeRate) throws CalculateMovementAmountException {
//        if (changeRate != null){
//            if (bankAccountCurrency.echangeRate.getInitialCcurrency().equals()bankAccountCurrency.getCode())
//            checkCurrencyConsistency(bankAccountCurrency.getCode())
//
//        }else{
//            throw new CalculateMovementAmountException("Change rate empty");
//        }
//        return Math.signum(amount)
//    }
}
