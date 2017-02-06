package kuvaldis.play.springframework;

import kuvaldis.play.springframework.aop.SomeService;
import kuvaldis.play.springframework.aop.UsageTracked;
import kuvaldis.play.springframework.conversionservice.BeanWithBeanWithSetOfStrings;
import kuvaldis.play.springframework.conversionservice.BeanWithListOfIntegers;
import kuvaldis.play.springframework.propertyeditor.BeanWithPersons;
import kuvaldis.play.springframework.qualifier.MovieRecommender;
import kuvaldis.play.springframework.scoperesolver.ScopeResolverBean;
import kuvaldis.play.springframework.validator.Address;
import kuvaldis.play.springframework.validator.AddressValidator;
import kuvaldis.play.springframework.validator.Customer;
import kuvaldis.play.springframework.validator.CustomerValidator;
import org.junit.Test;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ValidationUtils;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SpringframeworkTest {

    private final ApplicationContext context = new ClassPathXmlApplicationContext("test-context.xml");

    @Test
    public void testBeanPropertiesInitialization() throws Exception {
        final CoffeeConstructorBean bean1 = context.getBean(CoffeeConstructorBean.class);
        assertEquals("I have no idea", bean1.getSort());
        assertEquals("Oracle", bean1.getProducedBy());
        final CoffeeSettersBean bean2 = context.getBean(CoffeeSettersBean.class);
        assertEquals("Urmm... a good one", bean2.getSort());
        assertEquals("Juergen Hoeller", bean2.getProducedBy());
    }

    @Test
    public void testPropertiesMerge() throws Exception {
        final EmailsStorageBean emailsBean = context.getBean("emailsBean", EmailsStorageBean.class);
        assertEquals("admin@example.com", emailsBean.getEmails().getProperty("admin"));
        assertEquals("user@example.com", emailsBean.getEmails().getProperty("user"));
        final EmailsStorageBean emailsChildBean = context.getBean("emailsChildBean", EmailsStorageBean.class);
        assertEquals("eve@malefactor.com", emailsChildBean.getEmails().getProperty("admin"));
        assertEquals("user@example.com", emailsChildBean.getEmails().getProperty("user"));
    }

    @Test
    public void testCreatePrototypeFromSingleton() throws Exception {
        final CreatePrototypeFromSingleton.CommandManager manager = context.getBean(CreatePrototypeFromSingleton.CommandManager.class);
        final String output1 = manager.process("hello");
        assertEquals("Executed by (1): hello", output1);
        final String output2 = manager.process("world");
        assertEquals("Executed by (2): world", output2);
    }

    @Test
    public void testScopedProxy() throws Exception {
        final ExecutorService executor1 = Executors.newSingleThreadExecutor();
        final ExecutorService executor2 = Executors.newSingleThreadExecutor();

        final UseScopedProxy.First first = context.getBean(UseScopedProxy.First.class);

        final Future<Integer> future1 = executor1.submit(() -> first.getSecond().getValue());
        assertEquals(1, future1.get().intValue());

        final Future<Integer> future2 = executor2.submit(() -> first.getSecond().getValue());
        assertEquals(2, future2.get().intValue());
    }

    @Test
    public void testCustomQualifier() throws Exception {
        final MovieRecommender bean = context.getBean(MovieRecommender.class);
        assertEquals("Ace Ventura Pet Detective", bean.getComedy());
        assertEquals("Terminator", bean.getAction());
        assertEquals("The Shawshank Redemption", bean.getDrama());
    }

    @Test
    public void testScopeResolver() throws Exception {
        final ScopeResolverBean bean1 = context.getBean(ScopeResolverBean.class);
        final ScopeResolverBean bean2 = context.getBean(ScopeResolverBean.class);
        assertEquals(1, bean1.getCount());
        assertEquals(2, bean2.getCount());
    }

    @Test
    public void testNestedValidators() throws Exception {
        // given
        final Customer customer = new Customer("Henry VIII", 15, new Address("London, duh", ""));
        final CustomerValidator customerValidator = new CustomerValidator(new AddressValidator());

        // when
        final BeanPropertyBindingResult errors = new BeanPropertyBindingResult(customer, "customer");
        ValidationUtils.invokeValidator(customerValidator, customer, errors);

        // then
        assertEquals(2, errors.getErrorCount());
        final FieldError fieldError1 = errors.getFieldError("address.addressLine2");
        assertNotNull(fieldError1);
        assertTrue(Stream.of(fieldError1.getCodes())
                .anyMatch("required.customer.address.addressLine2"::equals));
        final FieldError fieldError2 = errors.getFieldError("age");
        assertNotNull(fieldError2);
        assertTrue(Stream.of(fieldError2.getCodes())
                .anyMatch("too.young.customer.age"::equals));
    }

    @Test
    public void testBeanWrapper() throws Exception {
        final BeanWrapper customer = new BeanWrapperImpl(new Customer());
        customer.setPropertyValue("name", "Sean");
        customer.setPropertyValue("address", new Address());
        customer.setPropertyValue("address.addressLine1", "Piedmont");

        assertEquals("Sean", ((Customer) customer.getWrappedInstance()).getName());
        assertEquals("Piedmont", ((Customer) customer.getWrappedInstance()).getAddress().getAddressLine1());
    }

    @Test
    public void testPropertyEditor() throws Exception {
        final BeanWithPersons bean = context.getBean(BeanWithPersons.class);
        assertEquals("John", bean.getPerson1().getFirstName());
        assertEquals("Doe", bean.getPerson1().getLastName());
        assertEquals("Agent", bean.getPerson2().getFirstName());
        assertEquals("Smith", bean.getPerson2().getLastName());
    }

    @Test
    public void testConversionService() throws Exception {
        final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("convertsion-service-context.xml");
        final BeanWithListOfIntegers bean = context.getBean(BeanWithListOfIntegers.class);
        assertEquals(Arrays.asList(1, 2, 3), bean.getIntegers());
    }

    @Test
    public void testCustomConverter() throws Exception {
        final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("convertsion-service-context.xml");
        final BeanWithBeanWithSetOfStrings bean = context.getBean(BeanWithBeanWithSetOfStrings.class);
        assertEquals(Stream.of("1", "2", "3").collect(Collectors.toSet()), bean.getBean().getStrings());
    }

    @Test
    public void testAopIntroduction() throws Exception {
        final SomeService bean = context.getBean(SomeService.class);
        bean.call();
        bean.call();
        bean.call();
        final UsageTracked usageTracked = (UsageTracked) bean;
        assertEquals(3, usageTracked.getCount());
    }
}
