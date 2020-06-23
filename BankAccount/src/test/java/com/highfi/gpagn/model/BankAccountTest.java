package com.highfi.gpagn.model;

import com.highfi.gpagn.helpers.TestHelper;
import com.highfi.gpagn.utils.ValidatorUtils;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class BankAccountTest {
    @Test
    public void test_bankAccount_with_valid_argument() {
        BankAccount bankAccount = TestHelper.acc1EurBankAccount;
        assertThat(ValidatorUtils.validator.validate(bankAccount)).isEmpty();

    }

    @Test
    public void test_bankAccount_with_null_arguments() {
        BankAccount bankAccount = BankAccount.builder().build();
        Set<ConstraintViolation<BankAccount>> violations = ValidatorUtils.validator.validate(bankAccount);
        assertThat(ValidatorUtils.validator.validate(bankAccount)).isNotEmpty();
        assertThat(formatMessages(violations)).containsExactlyInAnyOrder(
                "accountNumber must not be empty",
                "currency may not be null"
        );
    }

    @Test
    public void test_bankAccount_with_invalid_arguments() {
        BankAccount bankAccount = BankAccount.builder()
                .currency(Currency.builder().build())
                .build();

        Set<ConstraintViolation<BankAccount>> violations = ValidatorUtils.validator.validate(bankAccount);
        assertThat(ValidatorUtils.validator.validate(bankAccount)).isNotEmpty();
        assertThat(formatMessages(violations)).containsExactlyInAnyOrder(
                "accountNumber must not be empty",
                "currency.libelle may not be null",
                "currency.code may not be null"

        );

    }

    private List<String> formatMessages(Set<ConstraintViolation<BankAccount>> constraintViolations) {
        List<String> messages = new ArrayList<>();
        for (ConstraintViolation constraintViolation : constraintViolations) {
            messages.add(String.format("%s %s", constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage()));
        }
        return messages;
    }
}
