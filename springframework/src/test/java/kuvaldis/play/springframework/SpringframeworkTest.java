package kuvaldis.play.springframework;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.assertEquals;

public class SpringframeworkTest {

    final ApplicationContext context = new ClassPathXmlApplicationContext("test-context.xml");

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
}
