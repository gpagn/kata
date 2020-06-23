package com.highfi.gpagn.service;

import com.highfi.gpagn.exceptions.EntityValidationException;
import com.highfi.gpagn.exceptions.InvalidChangeRateException;
import com.highfi.gpagn.helpers.TestHelper;
import com.highfi.gpagn.model.Balance;
import com.highfi.gpagn.model.ChangeRate;
import com.highfi.gpagn.model.Movement;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class BalanceServiceTest {


    private BalanceServiceImpl balanceService;

    @Spy
    private MovementServiceImpl movementService;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();


    @Before
    public void setUp() {
        balanceService = new BalanceServiceImpl(movementService);
        Mockito.spy(balanceService);
    }

    @Test
    public void test_calculBalance_with_same_currency_return_2000() throws InvalidChangeRateException, EntityValidationException {
        //Given
        Balance balance = TestHelper.balance_Acc1EurBankAccount_1000;

        List<Movement> movements = new LinkedList<>();

        movements.add(TestHelper.mov_eurBA_eurMov_C_500);
        movements.add(TestHelper.mov_eurBA_eurMov_C_1000);
        movements.add(TestHelper.mov_eurBA_eurMov_D_500);

        List<ChangeRate> changeRates = new ArrayList<>();
        changeRates.add(TestHelper.changeRate_eur);

        //Then
        Balance newbalance = balanceService.calculBalance(balance, movements, changeRates);

        //Given
        assertThat(newbalance.getInitialBalance()).isEqualTo(2000.0);
        assertThat(DateUtils.isSameDay(newbalance.getDateBalance(), Calendar.getInstance().getTime())).isTrue();
        assertThat(newbalance.getBankAccount()).isEqualTo(TestHelper.acc1EurBankAccount);
    }

    @Test
    public void test_calculBalance_with_different_currency_return_1425() throws InvalidChangeRateException, EntityValidationException {
        //Given
        Balance balance = TestHelper.balance_Acc1EurBankAccount_1000;

        List<Movement> movements = new LinkedList<>();

        movements.add(TestHelper.mov_eurBA_eurMov_C_500);
        movements.add(TestHelper.mov_eurBA_USDMov_C_500);
        movements.add(TestHelper.mov_eurBA_eurMov_D_500);

        List<ChangeRate> changeRates = new ArrayList<>();
        changeRates.add(TestHelper.changeRate_eur);
        changeRates.add(TestHelper.changeRate_usd);

        //Then
        Balance newbalance = balanceService.calculBalance(balance, movements, changeRates);

        //Given
        assertThat(newbalance.getInitialBalance()).isEqualTo(1425.0);
        assertThat(DateUtils.isSameDay(newbalance.getDateBalance(), Calendar.getInstance().getTime())).isTrue();
        assertThat(newbalance.getBankAccount()).isEqualTo(TestHelper.acc1EurBankAccount);
    }

    @Test
    public void test_calculBalance_return_minus1000() throws InvalidChangeRateException, EntityValidationException {
        //Given
        Balance balance = TestHelper.balance_Acc1EurBankAccount_1000;

        List<Movement> movements = new LinkedList<>();

        movements.add(TestHelper.mov_eurBA_eurMov_C_500);
        movements.add(TestHelper.mov_eurBA_eurMov_D_500);
        movements.add(TestHelper.mov_eurBA_eurMov_D_1000);
        movements.add(TestHelper.mov_eurBA_eurMov_D_1000);

        List<ChangeRate> changeRates = new ArrayList<>();
        changeRates.add(TestHelper.changeRate_eur);
        //Then
        Balance newbalance = balanceService.calculBalance(balance, movements, changeRates);

        //Given
        assertThat(newbalance.getInitialBalance()).isEqualTo(-1000.0);
        assertThat(DateUtils.isSameDay(newbalance.getDateBalance(), Calendar.getInstance().getTime())).isTrue();
        assertThat(newbalance.getBankAccount()).isEqualTo(TestHelper.acc1EurBankAccount);

    }

    @Test
    public void test_calculBalance_when_changeRate_date_is_invalid() throws InvalidChangeRateException, EntityValidationException {
        //Given
        Balance balance = TestHelper.balance_Acc1EurBankAccount_1000;

        List<Movement> movements = new LinkedList<>();
        movements.add(TestHelper.mov_eurBA_eurMov_C_500);
        movements.add(TestHelper.mov_eurBA_eurMov_D_500);
        movements.add(TestHelper.mov_eurBA_eurMov_D_1000);
        movements.add(TestHelper.mov_eurBA_eurMov_D_1000);

        List<ChangeRate> changeRates = new ArrayList<>();
        changeRates.add(
                ChangeRate.builder()
                        .rate(1.0)
                        .rateDate(TestHelper.getDate(2020, Month.FEBRUARY.getValue(), 2))
                        .initialCurrency(TestHelper.euroCurrency)
                        .convertedCurrency(TestHelper.euroCurrency)
                        .build());
        //Given
        exceptionRule.expect(InvalidChangeRateException.class);

        //Then
        Balance newbalance = balanceService.calculBalance(balance, movements, changeRates);


    }

}
