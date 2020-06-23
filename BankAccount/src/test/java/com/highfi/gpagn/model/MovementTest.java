package com.highfi.gpagn.model;

import com.highfi.gpagn.helpers.TestHelper;
import com.highfi.gpagn.utils.ValidatorUtils;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


public class MovementTest {

    @Test
    public void test_movement_with_valid_argument() {
        Movement movement = TestHelper.mov_eurBA_eurMov_C_500;
        assertThat(ValidatorUtils.validator.validate(movement)).isEmpty();

    }

    @Test
    public void test_movement_with_null_arguments() {
        Movement movement = Movement.builder().build();
        Set<ConstraintViolation<Movement>> violations = ValidatorUtils.validator.validate(movement);
        assertThat(ValidatorUtils.validator.validate(movement)).isNotEmpty();
        assertThat(formatMessages(violations)).containsExactlyInAnyOrder(
                "movementType may not be null",
                "sens must not be empty",
                "amount must be greater than 0",
                "tradeDate may not be null",
                "movementCurrency may not be null",
                "receiverBankAccount may not be null"
        );
    }

    @Test
    public void test_movement_with_invalid_arguments() {
        Movement movement = Movement.builder()
                .receiverBankAccount(BankAccount.builder().currency(Currency.builder().build()).build())
                .amount(-10.0)
                .movementCurrency(Currency.builder().build())
                .build();

        Set<ConstraintViolation<Movement>> violations = ValidatorUtils.validator.validate(movement);
        assertThat(ValidatorUtils.validator.validate(movement)).isNotEmpty();
        assertThat(formatMessages(violations)).containsExactlyInAnyOrder(
                "movementType may not be null",
                "amount must be greater than 0",
                "tradeDate may not be null",
                "sens must not be empty",
                "movementCurrency.code may not be null",
                "movementCurrency.libelle may not be null",
                "receiverBankAccount.accountNumber must not be empty",
                "receiverBankAccount.currency.code may not be null",
                "receiverBankAccount.currency.libelle may not be null"

        );

    }

    private List<String> formatMessages(Set<ConstraintViolation<Movement>> constraintViolations) {
        List<String> messages = new ArrayList<>();
        for (ConstraintViolation constraintViolation : constraintViolations) {
            messages.add(String.format("%s %s", constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage()));
        }
        return messages;
    }
}
