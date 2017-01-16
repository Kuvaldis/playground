package kuvaldis.play.springframework.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class CustomerValidator implements Validator {

    private final AddressValidator addressValidator;

    public CustomerValidator(final AddressValidator addressValidator) {
        if (addressValidator == null) {
            throw new IllegalArgumentException("The supplied [Validator] is " +
                    "required and must not be null.");
        }
        if (!addressValidator.supports(Address.class)) {
            throw new IllegalArgumentException("The supplied [Validator] must " +
                    "support the validation of [Address] instances.");
        }
        this.addressValidator = addressValidator;
    }

    @Override
    public boolean supports(final Class<?> clazz) {
        return Customer.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "required");
        final Customer customer = (Customer) target;
        if (customer.getAge() < 18) {
            errors.rejectValue("age", "too.young");
        } else if (customer.getAge() > 60) {
            errors.rejectValue("age", "too.old");
        }
        try {
            errors.pushNestedPath("address");
            ValidationUtils.invokeValidator(addressValidator, customer.getAddress(), errors);
        } finally {
            errors.popNestedPath();
        }
    }
}
