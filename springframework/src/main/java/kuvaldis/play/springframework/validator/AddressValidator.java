package kuvaldis.play.springframework.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class AddressValidator implements Validator {

    @Override
    public boolean supports(final Class<?> clazz) {
        return Address.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "addressLine1", "required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "addressLine2", "required");
    }
}
