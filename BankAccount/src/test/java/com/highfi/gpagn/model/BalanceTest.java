package com.highfi.gpagn.model;

import com.highfi.gpagn.helpers.TestHelper;
import com.highfi.gpagn.utils.ValidatorUtils;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class BalanceTest {
    @Test
    public void test_balance_with_valid_argument() {
        Balance balance = TestHelper.balance_Acc1EurBankAccount_1000;
        assertThat(ValidatorUtils.validator.validate(balance)).isEmpty();

    }

    @Test
    public void test_bankAccount_with_null_arguments() {
        Balance balance = Balance.builder().build();
        Set<ConstraintViolation<Balance>> violations = ValidatorUtils.validator.validate(balance);
        assertThat(ValidatorUtils.validator.validate(balance)).isNotEmpty();
        assertThat(formatMessages(violations)).containsExactlyInAnyOrder(
                "bankAccount may not be null",
                "dateBalance may not be null",
                "initialBalance may not be null"
        );
    }

    @Test
    public void test_balance_with_invalid_arguments() {
        Balance balance = Balance.builder()
                .bankAccount(BankAccount.builder().build())
                .build();

        Set<ConstraintViolation<Balance>> violations = ValidatorUtils.validator.validate(balance);
        assertThat(ValidatorUtils.validator.validate(balance)).isNotEmpty();
        assertThat(formatMessages(violations)).containsExactlyInAnyOrder(
                "dateBalance may not be null",
                "initialBalance may not be null",
                "bankAccount.accountNumber must not be empty",
                "bankAccount.currency may not be null"

        );

    }

    private List<String> formatMessages(Set<ConstraintViolation<Balance>> constraintViolations) {
        List<String> messages = new ArrayList<>();
        for (ConstraintViolation constraintViolation : constraintViolations) {
            messages.add(String.format("%s %s", constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage()));
        }
        return messages;
    }
}
