package kuvaldis.play.cglib;

import net.sf.cglib.beans.BeanGenerator;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;

public class BeanGeneratorTest {

    @Test
    public void testBeanGenerator() throws Exception {
        final BeanGenerator beanGenerator = new BeanGenerator();
        beanGenerator.addProperty("value", String.class);
        final Object bean = beanGenerator.create();
        final Method setter = bean.getClass().getMethod("setValue", String.class);
        setter.invoke(bean, "Hello, bean!");
        final Method getter = bean.getClass().getMethod("getValue");
        assertEquals("Hello, bean!", getter.invoke(bean));
    }
}
