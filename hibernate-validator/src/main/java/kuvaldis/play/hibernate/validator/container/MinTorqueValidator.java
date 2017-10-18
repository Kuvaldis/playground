package kuvaldis.play.hibernate.validator.container;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MinTorqueValidator implements ConstraintValidator<MinTorque, Gear> {

    private volatile int minTorque;

    @Override
    public void initialize(final MinTorque constraintAnnotation) {
        this.minTorque = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(final Gear value, final ConstraintValidatorContext context) {
        return value.getTorque() >= minTorque;
    }
}
