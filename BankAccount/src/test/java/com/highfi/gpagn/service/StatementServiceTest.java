package com.highfi.gpagn.service;

import com.highfi.gpagn.exceptions.EntityValidationException;
import com.highfi.gpagn.exceptions.InvalidChangeRateException;
import com.highfi.gpagn.helpers.TestHelper;
import com.highfi.gpagn.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class StatementServiceTest {

    private StatementService statementService;

    @Spy
    private MovementServiceImpl movementService;

    @Before
    public void setUp() {
        statementService = new StatementServiceImpl(movementService);
        Mockito.spy(statementService);
    }

    @Test
    public void test_generateStatement() throws InvalidChangeRateException, EntityValidationException {
        //Given
        BankAccount bankAccount = TestHelper.acc1EurBankAccount;

        Balance balance = TestHelper.balance_Acc1EurBankAccount_1000;

        Date tradeDate1 = TestHelper.getDate(2020, Calendar.JANUARY, 1);
        Date tradeDate2 = TestHelper.getDate(2020, Calendar.JANUARY, 2);
        Date tradeDate3 = TestHelper.getDate(2020, Calendar.JANUARY, 3);

        List<Movement> movements = new ArrayList<>();
        Movement movement1 = TestHelper.mov_eurBA_eurMov_C_500;
        movement1.setTradeDate(tradeDate1);

        Movement movement2 = TestHelper.mov_eurBA_eurMov_D_1000;
        movement2.setTradeDate(tradeDate2);

        Movement movement3 = TestHelper.mov_eurBA_USDMov_C_500;
        movement3.setTradeDate(tradeDate3);

        movements.add(movement1);
        movements.add(movement2);
        movements.add(movement3);

        List<ChangeRate> changeRates = new ArrayList<>();
        ChangeRate changeRate_eur_1 = ChangeRate.builder()
                .initialCurrency(TestHelper.euroCurrency)
                .convertedCurrency(TestHelper.euroCurrency)
                .rateDate(TestHelper.getDate(2020, Calendar.JANUARY, 1))
                .rate(1.0)
                .build();

        changeRates.add(changeRate_eur_1);

        ChangeRate changeRate_eur_2 = ChangeRate.builder()
                .initialCurrency(TestHelper.euroCurrency)
                .convertedCurrency(TestHelper.euroCurrency)
                .rateDate(TestHelper.getDate(2020, Calendar.JANUARY, 2))
                .rate(1.0)
                .build();
        changeRates.add(changeRate_eur_2);

        ChangeRate changeRate_eur_3 = ChangeRate.builder()
                .initialCurrency(TestHelper.euroCurrency)
                .convertedCurrency(TestHelper.dollarCurrency)
                .rateDate(TestHelper.getDate(2020, Calendar.JANUARY, 3))
                .rate(0.85)
                .build();
        changeRates.add(changeRate_eur_3);

        Date startDate = TestHelper.getDate(2020, Calendar.JANUARY, 1);
        Date endDate = TestHelper.getDate(2020, Calendar.JANUARY, 31);

        //When
        Statement statement = statementService.generateStatement(startDate, endDate, balance, movements, changeRates);
        System.out.println(statement);
        //Then
        assertThat(statement.getStartDate()).isEqualTo(startDate);
        assertThat(statement.getEndDate()).isEqualTo(endDate);
        assertThat(statement.getBankAccount()).isEqualTo(bankAccount);
        assertThat(statement.getStatementLines()).isNotEmpty().hasSize(3);
    }

    @Test
    public void test_printStatement() throws InvalidChangeRateException, EntityValidationException {
        //Given
        BankAccount bankAccount = TestHelper.acc1EurBankAccount;

        Balance balance = TestHelper.balance_Acc1EurBankAccount_1000;

        Date tradeDate1 = TestHelper.getDate(2020, Calendar.JANUARY, 1);
        Date tradeDate2 = TestHelper.getDate(2020, Calendar.JANUARY, 2);
        Date tradeDate3 = TestHelper.getDate(2020, Calendar.JANUARY, 3);

        List<Movement> movements = new ArrayList<>();
        Movement movement1 = TestHelper.mov_eurBA_eurMov_C_500;
        movement1.setTradeDate(tradeDate1);

        Movement movement2 = TestHelper.mov_eurBA_eurMov_D_1000;
        movement2.setTradeDate(tradeDate2);

        Movement movement3 = TestHelper.mov_eurBA_USDMov_C_500;
        movement3.setTradeDate(tradeDate3);

        movements.add(movement1);
        movements.add(movement2);
        movements.add(movement3);

        List<ChangeRate> changeRates = new ArrayList<>();
        ChangeRate changeRate_eur_1 = ChangeRate.builder()
                .initialCurrency(TestHelper.euroCurrency)
                .convertedCurrency(TestHelper.euroCurrency)
                .rateDate(TestHelper.getDate(2020, Calendar.JANUARY, 1))
                .rate(1.0)
                .build();

        changeRates.add(changeRate_eur_1);

        ChangeRate changeRate_eur_2 = ChangeRate.builder()
                .initialCurrency(TestHelper.euroCurrency)
                .convertedCurrency(TestHelper.euroCurrency)
                .rateDate(TestHelper.getDate(2020, Calendar.JANUARY, 2))
                .rate(1.0)
                .build();
        changeRates.add(changeRate_eur_2);

        ChangeRate changeRate_eur_3 = ChangeRate.builder()
                .initialCurrency(TestHelper.euroCurrency)
                .convertedCurrency(TestHelper.dollarCurrency)
                .rateDate(TestHelper.getDate(2020, Calendar.JANUARY, 3))
                .rate(0.85)
                .build();
        changeRates.add(changeRate_eur_3);

        Date startDate = TestHelper.getDate(2020, Calendar.JANUARY, 1);
        Date endDate = TestHelper.getDate(2020, Calendar.JANUARY, 31);

        //When
        Statement statement = statementService.generateStatement(startDate, endDate, balance, movements, changeRates);
        String print = statementService.printStatement(statement);

        //then
        assertThat(print).isEqualTo("OPERATION | DATE | AMOUNT | BALANCE\n" +
                "DEPOSIT | 01/01/2020 | 500,00 | 1500,00\n" +
                "WITHDRAWAL | 02/01/2020 | -1000,00 | 500,00\n" +
                "DEPOSIT | 03/01/2020 | 425,00 | 925,00\n");

    }
}
