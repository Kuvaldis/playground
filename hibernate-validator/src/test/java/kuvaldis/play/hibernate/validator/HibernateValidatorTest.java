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
import javax.validation.constraints.Max;
import javax.validation.executable.ExecutableValidator;
import javax.validation.groups.Default;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HibernateValidatorTest {

    private static Validator validator;

    @BeforeClass
    public static void setUp() throws Exception {
        final ValidatorFactory validatorFactory = Validation.byDefaultProvider()
                .configure()
                .addValueExtractor(new GearBoxValueExtractor())
                .buildValidatorFactory();
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
        final Car car = createValidCar();
        car.setGearBox(new GearBox<>(new Gear.OldGear()));
        final Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertEquals(1, violations.size());
        final ConstraintViolation<Car> violation = violations.iterator().next();
        assertEquals("Gear is not providing enough torque.", violation.getMessage());
        assertEquals("gearBox", violation.getPropertyPath().toString());
    }

    @Test
    public void validateSingleProperty() throws Exception {
        final Car car = createValidCar();
        car.setLicensePlate("S");
        final Set<ConstraintViolation<Car>> violations = validator.validateProperty(car, "manufacturer");
        assertTrue(violations.isEmpty());
    }

    @Test
    public void validateInputParameter() throws Exception {
        final Car car = createValidCar();
        final Method drive = Car.class.getMethod("drive", int.class);
        final Object[] parameterValues = {80};
        final ExecutableValidator executableValidator = validator.forExecutables();
        final Set<ConstraintViolation<Car>> violations = executableValidator.validateParameters(car, drive, parameterValues);
        assertEquals(1, violations.size());
        Class<? extends Annotation> constraintType = violations.iterator()
                .next()
                .getConstraintDescriptor()
                .getAnnotation()
                .annotationType();
        assertEquals(Max.class, constraintType);
    }

    @Test
    public void validateGroups() throws Exception {
        final Car car = createValidCar();
        car.setPassedVehicleInspection(false);
        car.getDriver().setHasDrivingLicense(false);

        Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertEquals(0, violations.size());

        violations = validator.validate(car, CarChecks.class);
        assertEquals(1, violations.size());
        assertEquals("The car has to pass the vehicle inspection first", violations.iterator().next().getMessage());

        violations = validator.validate(car, DriverChecks.class);
        assertEquals(1, violations.size());
        assertEquals("You first have to pass the driving test", violations.iterator().next().getMessage());

        car.setPassedVehicleInspection(true);
        car.getDriver().setHasDrivingLicense(true);
        assertEquals(0, validator.validate(car, Default.class, CarChecks.class, DriverChecks.class).size());
    }

    @Test
    public void testCustomCaseValidator() throws Exception {
        final Car car = createValidCar();
        car.setLicensePlate("dd-ab-123");
        final Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertEquals(1, violations.size());
        assertEquals("Case mode must be UPPER.", violations.iterator().next().getMessage());
    }

    private Car createValidCar() {
        final Car car = new Car();
        car.setManufacturer("Toyota");
        car.setLicensePlate("DD-AB-123");
        car.setSeatCount(4);
        car.setGearBox(new GearBox<>(new Gear(200)));
        car.setPassedVehicleInspection(true);
        final Driver driver = new Driver();
        driver.setAge(21);
        driver.setHasDrivingLicense(true);
        car.setDriver(driver);
        return car;
    }
}