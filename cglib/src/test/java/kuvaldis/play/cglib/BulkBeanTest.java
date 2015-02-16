package kuvaldis.play.cglib;

import net.sf.cglib.beans.BulkBean;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class BulkBeanTest {

    @Test
    public void testBulkBean() throws Exception {
        final BulkBean bulkBean = BulkBean.create(SamplePropertiesBean.class,
                new String[]{"getProperty1", "getProperty2"},
                new String[]{"setProperty1", "setProperty2"},
                new Class[]{String.class, String.class});
        final SamplePropertiesBean bean = new SamplePropertiesBean();
        bean.setProperty1("Hello, property1!");
        bean.setProperty2("Hello, property2!");
        assertEquals(2, bulkBean.getPropertyValues(bean).length);
        assertArrayEquals(new String[]{"Hello, property1!", "Hello, property2!"},
                bulkBean.getPropertyValues(bean));
        bulkBean.setPropertyValues(bean, new String[] {"Goodbye, property1!", "Goodbye, property2!"});
        assertArrayEquals(new String[]{"Goodbye, property1!", "Goodbye, property2!"},
                bulkBean.getPropertyValues(bean));
    }
}
