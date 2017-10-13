package kuvaldis.play.hibernate.validator;

import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Arrays;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class HibernateValidatorTest {

    private static Validator validator;

    @BeforeClass
    public static void setUp() throws Exception {
        final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void fieldConstraintsTest() throws Exception {
        final Car car = new Car(null, "DD-AB-123", 4);
        final Set<ConstraintViolation<Car>> violations = validator.validate(car);

        assertEquals(1, violations.size());
        assertEquals("must not be null", violations.iterator().next().getMessage());
    }

    @Test
    public void parameterTest() throws Exception {
        final Toolbox toolbox = new Toolbox(Arrays.asList("drill", "screwdriver", null));
        final Set<ConstraintViolation<Toolbox>> violations = validator.validate(toolbox);

        assertEquals(1, violations.size());
        // if it was set there would not be index, just []
        assertEquals("tools[2].<list element>", violations.iterator().next().getPropertyPath().toString());
    }
}