package com.highfi.gpagn.helpers;

import com.highfi.gpagn.model.*;
import com.highfi.gpagn.model.enums.MovementType;
import com.highfi.gpagn.model.enums.Sens;

import java.util.Calendar;
import java.util.Date;

public class TestHelper {


    public static Date getDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTime();
    }

    //Date
    public static Date date_2020_06_1 = getDate(2020, Calendar.JUNE, 1);

    //Currency
    public static Currency euroCurrency = Currency.builder().code("EUR").libelle("Euro").build();
    public static Currency dollarCurrency = Currency.builder().code("USD").libelle("Dollar").build();

    //BankAccount
    public static BankAccount acc1EurBankAccount = BankAccount.builder()
            .currency(euroCurrency)
            .accountNumber("Acc1")
            .build();

    //Balance
    public static Balance balance_Acc1EurBankAccount_1000 = Balance.builder().
            dateBalance(date_2020_06_1).
            initialBalance(1000.0).
            bankAccount(acc1EurBankAccount)
            .build();

    //Movement
    public static Movement mov_eurBA_eurMov_C_1000 = Movement.builder()
            .movementCurrency(euroCurrency)
            .movementType(MovementType.DEPOSIT)
            .receiverBankAccount(acc1EurBankAccount)
            .sens(Sens.CREDIT)
            .tradeDate(date_2020_06_1)
            .amount(1000)
            .build();
    public static Movement mov_eurBA_eurMov_C_500 = Movement.builder()
            .movementCurrency(euroCurrency)
            .movementType(MovementType.DEPOSIT)
            .receiverBankAccount(acc1EurBankAccount)
            .sens(Sens.CREDIT)
            .tradeDate(date_2020_06_1)
            .amount(500.0)
            .build();
    public static Movement mov_eurBA_eurMov_D_1000 = Movement.builder()
            .movementCurrency(euroCurrency)
            .movementType(MovementType.WITHDRAWAL)
            .receiverBankAccount(acc1EurBankAccount)
            .sens(Sens.DEBIT)
            .tradeDate(date_2020_06_1)
            .amount(1000)
            .build();
    public static Movement mov_eurBA_eurMov_D_500 = Movement.builder()
            .movementCurrency(euroCurrency)
            .movementType(MovementType.WITHDRAWAL)
            .receiverBankAccount(acc1EurBankAccount)
            .sens(Sens.DEBIT)
            .tradeDate(date_2020_06_1)
            .amount(500.0)
            .build();
    public static Movement mov_eurBA_USDMov_C_1000 = Movement.builder()
            .movementCurrency(dollarCurrency)
            .movementType(MovementType.DEPOSIT)
            .receiverBankAccount(acc1EurBankAccount)
            .sens(Sens.CREDIT)
            .tradeDate(date_2020_06_1)
            .amount(1000)
            .build();
    public static Movement mov_eurBA_USDMov_C_500 = Movement.builder()
            .movementCurrency(dollarCurrency)
            .movementType(MovementType.DEPOSIT)
            .receiverBankAccount(acc1EurBankAccount)
            .sens(Sens.CREDIT)
            .tradeDate(date_2020_06_1)
            .amount(500.0)
            .build();
    public static Movement mov_eurBA_USDMov_D_1000 = Movement.builder()
            .movementCurrency(dollarCurrency)
            .movementType(MovementType.WITHDRAWAL)
            .receiverBankAccount(acc1EurBankAccount)
            .sens(Sens.DEBIT)
            .tradeDate(date_2020_06_1)
            .amount(1000)
            .build();
    public static Movement mov_eurBA_USDMov_D_500 = Movement.builder()
            .movementCurrency(dollarCurrency)
            .movementType(MovementType.WITHDRAWAL)
            .receiverBankAccount(acc1EurBankAccount)
            .sens(Sens.DEBIT)
            .tradeDate(date_2020_06_1)
            .amount(500.0)
            .build();

    //ChangeRate
    public static ChangeRate changeRate_usd = ChangeRate.builder()
            .initialCurrency(euroCurrency)
            .convertedCurrency(dollarCurrency)
            .rateDate(date_2020_06_1)
            .rate(0.85)
            .build();
    public static ChangeRate changeRate_eur = ChangeRate.builder()
            .initialCurrency(euroCurrency)
            .convertedCurrency(euroCurrency)
            .rateDate(date_2020_06_1)
            .rate(1.0)
            .build();

}
