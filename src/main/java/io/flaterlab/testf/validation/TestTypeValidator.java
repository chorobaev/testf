package io.flaterlab.testf.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;
import java.util.TreeSet;

public class TestTypeValidator implements ConstraintValidator<ValidTestType, String> {

    private static Set<String> validTypes = new TreeSet<>();

    static {
        validTypes.add("TEST");
        validTypes.add("QUIZ");
    }

    @Override
    public boolean isValid(String type, ConstraintValidatorContext context) {
        return validTypes.contains(type);
    }
}
