package kuvaldis.play.hibernate.validator;

import kuvaldis.play.hibernate.validator.container.Gear;
import kuvaldis.play.hibernate.validator.container.GearBox;
import kuvaldis.play.hibernate.validator.container.GearBoxValueExtractor;
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
        final Car car = createValidCar();
        car.setManufacturer(null);
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

    @Test
    public void testCustomContainer() throws Exception {
        // register value extractor first
        final ValidatorFactory validatorFactory = Validation.byDefaultProvider()
                .configure()
                .addValueExtractor(new GearBoxValueExtractor())
                .buildValidatorFactory();
        final Validator validator = validatorFactory.getValidator();
        final Car car = createValidCar();
        car.setGearBox(new GearBox<>(new Gear.OldGear()));
        final Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertEquals(1, violations.size());
        final ConstraintViolation<Car> violation = violations.iterator().next();
        assertEquals("Gear is not providing enough torque.", violation.getMessage());
        assertEquals("gearBox", violation.getPropertyPath().toString());
    }

    private Car createValidCar() {
        final Car car = new Car();
        car.setManufacturer("Toyota");
        car.setLicensePlate("DD-AB-123");
        car.setSeatCount(4);
        car.setGearBox(new GearBox<>(new Gear(200)));
        return car;
    }
}