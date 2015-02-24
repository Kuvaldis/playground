package kuvaldis.play.cglib;

import net.sf.cglib.beans.BeanMap;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BeanMapTest {

    @Test
    public void testBeanMap() throws Exception {
        final SampleBean bean = new SampleBean();
        final BeanMap beanMap = BeanMap.create(bean);
        bean.setValue("Hello, world!");
        assertEquals("Hello, world!", beanMap.get("value"));
    }
}
