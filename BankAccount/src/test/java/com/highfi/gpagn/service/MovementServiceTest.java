package com.highfi.gpagn.service;

import com.highfi.gpagn.exceptions.EntityValidationException;
import com.highfi.gpagn.exceptions.InvalidChangeRateException;
import com.highfi.gpagn.helpers.TestHelper;
import com.highfi.gpagn.model.BankAccount;
import com.highfi.gpagn.model.ChangeRate;
import com.highfi.gpagn.model.Currency;
import com.highfi.gpagn.model.Movement;
import com.highfi.gpagn.model.enums.MovementType;
import com.highfi.gpagn.model.enums.Sens;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class MovementServiceTest {

    @Spy
    private MovementServiceImpl movementService;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void test_when_createDepositMovement_with_valid_arguments_then_credit_movement() throws EntityValidationException {
        Currency currency = TestHelper.euroCurrency;
        BankAccount bankAccount = TestHelper.acc1EurBankAccount;

        Movement movement = movementService.createDepositMovement(bankAccount, 1000.0, currency);
        assertThat(movement).isNotNull();
        assertThat(movement.getMovementCurrency()).isEqualTo(currency);
        assertThat(movement.getAmount()).isEqualTo(1000.0);
        assertThat(movement.getSens()).isEqualTo(Sens.CREDIT);
        assertThat(movement.getDescription()).isNull();
        assertThat(movement.getMovementType()).isEqualTo(MovementType.DEPOSIT);
        assertThat(movement.getReceiverBankAccount()).isEqualTo(bankAccount);
        assertThat(movement.getTradeDate()).isNotNull();

    }

    @Test
    public void test_when_createWithdrawalMovement_with_valid_arguments_then_debit_movement() throws EntityValidationException {
        Currency currency = TestHelper.euroCurrency;
        BankAccount bankAccount = TestHelper.acc1EurBankAccount;

        Movement movement = movementService.createWithdrawalMovement(bankAccount, 1000.0, currency);
        assertThat(movement).isNotNull();
        assertThat(movement.getMovementCurrency()).isEqualTo(currency);
        assertThat(movement.getAmount()).isEqualTo(1000.0);
        assertThat(movement.getSens()).isEqualTo(Sens.DEBIT);
        assertThat(movement.getDescription()).isNull();
        assertThat(movement.getMovementType()).isEqualTo(MovementType.DEPOSIT);
        assertThat(movement.getReceiverBankAccount()).isEqualTo(bankAccount);
        assertThat(movement.getTradeDate()).isNotNull();
    }


    @Test
    public void test_when_createDepositMovement_with_invalid_arguments_then_exception() throws EntityValidationException {
        Currency currency = Currency.builder().build();
        BankAccount bankAccount = TestHelper.acc1EurBankAccount;
        exceptionRule.expect(EntityValidationException.class);
        movementService.createDepositMovement(bankAccount, 1000.0, currency);

    }

    @Test
    public void test_when_createWithdrawalMovement_with_invalid_arguments_then_exception() throws EntityValidationException {
        Currency currency = Currency.builder().build();
        BankAccount bankAccount = TestHelper.acc1EurBankAccount;
        exceptionRule.expect(EntityValidationException.class);
        movementService.createWithdrawalMovement(bankAccount, 1000.0, currency);

    }

    @Test
    public void test_calulCreditMovementAmount_with_same_currency() throws InvalidChangeRateException, EntityValidationException {
        //Given

        Movement movement = TestHelper.mov_eurBA_eurMov_C_1000;
        List<ChangeRate> changeRates = new ArrayList<>();
        changeRates.add(TestHelper.changeRate_eur);

        //When
        double amount = movementService.calculateMovementAmount(movement, changeRates);

        //Then
        assertThat(amount).isEqualTo(1000.0);
    }

    @Test
    public void test_calulCreditMovementAmount_with_different_currency() throws InvalidChangeRateException, EntityValidationException {
        //Given

        Movement movement = TestHelper.mov_eurBA_USDMov_C_500;
        List<ChangeRate> changeRates = new ArrayList<>();
        changeRates.add(TestHelper.changeRate_usd);

        //When
        double amount = movementService.calculateMovementAmount(movement, changeRates);

        //Then
        assertThat(amount).isEqualTo(425.0);
    }

    @Test
    public void test_calulCreditMovementAmount_with_different_currency_and_invalidChangeRate_then_exception() throws InvalidChangeRateException, EntityValidationException {
        //Given

        Movement movement = TestHelper.mov_eurBA_USDMov_C_500;
        List<ChangeRate> changeRates = new ArrayList<>();
        changeRates.add(TestHelper.changeRate_eur);

        //Then
        exceptionRule.expect(InvalidChangeRateException.class);

        //When
        double amount = movementService.calculateMovementAmount(movement, changeRates);


    }

    @Test
    public void test_calulCreditMovementAmount_with_different_currency_and_rate_0_then_exception() throws InvalidChangeRateException, EntityValidationException {
        //Given

        Movement movement = TestHelper.mov_eurBA_USDMov_C_500;
        List<ChangeRate> changeRates = new ArrayList<>();
        changeRates.add(ChangeRate.builder()
                .convertedCurrency(TestHelper.dollarCurrency)
                .initialCurrency(TestHelper.euroCurrency)
                .rateDate(TestHelper.date_2020_06_1)
                .rate(0.0)
                .build());

        //Then
        exceptionRule.expect(EntityValidationException.class);

        //When
        double amount = movementService.calculateMovementAmount(movement, changeRates);


    }

    @Test
    public void test_calulCreditMovementAmount_with_different_currency_and_rate_date_different_movement_date_then_exception() throws InvalidChangeRateException, EntityValidationException {
        //Given

        Movement movement = TestHelper.mov_eurBA_USDMov_C_500;
        List<ChangeRate> changeRates = new ArrayList<>();
        changeRates.add(ChangeRate.builder()
                .convertedCurrency(TestHelper.dollarCurrency)
                .initialCurrency(TestHelper.euroCurrency)
                .rateDate(TestHelper.getDate(2020, Calendar.JULY, 1))
                .rate(0.85)
                .build());

        //Then
        exceptionRule.expect(InvalidChangeRateException.class);

        //When
        double amount = movementService.calculateMovementAmount(movement, changeRates);


    }

}
