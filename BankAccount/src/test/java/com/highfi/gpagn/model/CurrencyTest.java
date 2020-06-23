package com.highfi.gpagn.model;

import com.highfi.gpagn.helpers.TestHelper;
import com.highfi.gpagn.utils.ValidatorUtils;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class CurrencyTest {

    @Test
    public void test_currency_with_valid_argument() {
        Currency currency = TestHelper.euroCurrency;
        assertThat(ValidatorUtils.validator.validate(currency)).isEmpty();

    }

    @Test
    public void test_currency_with_null_arguments() {
        Currency currency = Currency.builder().build();
        Set<ConstraintViolation<Currency>> violations = ValidatorUtils.validator.validate(currency);
        assertThat(ValidatorUtils.validator.validate(currency)).isNotEmpty();
        assertThat(formatMessages(violations)).containsExactlyInAnyOrder(
                "libelle may not be null",
                "code may not be null"
        );
    }

    private List<String> formatMessages(Set<ConstraintViolation<Currency>> constraintViolations) {
        List<String> messages = new ArrayList<>();
        for (ConstraintViolation constraintViolation : constraintViolations) {
            messages.add(String.format("%s %s", constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage()));
        }
        return messages;
    }
}
