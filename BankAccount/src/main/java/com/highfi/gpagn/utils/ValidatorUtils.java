package com.highfi.gpagn.utils;

import org.apache.bval.jsr.ApacheValidationProvider;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ValidatorUtils {

    public static ValidatorFactory validatorFactory
            = Validation.byProvider(ApacheValidationProvider.class)
            .configure().buildValidatorFactory();

    public static Validator validator = validatorFactory.getValidator();

    public static List<String> getMessage(Set<ConstraintViolation<?>> violations) {
        List<String> messages = new ArrayList<>();
        for (ConstraintViolation violation : violations) {
            messages.add(String.join(violation.getPropertyPath().toString(), " ", violation.getMessage()));

        }
        return messages;
    }


}
