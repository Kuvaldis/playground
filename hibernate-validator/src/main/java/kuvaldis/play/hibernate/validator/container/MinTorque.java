package kuvaldis.play.hibernate.validator.container;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = {MinTorqueValidator.class})
public @interface MinTorque {

    String message() default "Gear is not providing enough torque.";

    int value();

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
