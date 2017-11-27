package kuvaldis.play.hibernate.validator;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;

public class Driver extends Person{

    @Min(value = 18, message = "You have to be 18 to drive a car", groups = DriverChecks.class)
    private int age;

    @AssertTrue(message = "You first have to pass the driving test", groups = DriverChecks.class)
    private boolean hasDrivingLicense;

    public int getAge() {
        return age;
    }

    public void setAge(final int age) {
        this.age = age;
    }

    public boolean isHasDrivingLicense() {
        return hasDrivingLicense;
    }

    public void setHasDrivingLicense(final boolean hasDrivingLicense) {
        this.hasDrivingLicense = hasDrivingLicense;
    }
}
