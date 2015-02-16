package kuvaldis.play.cglib;

import net.sf.cglib.reflect.MethodDelegate;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MethodDelegateTest {

    @Test
    public void testMethodDelegate() throws Exception {
        SampleBean bean = new SampleBean();
        bean.setValue("Hello world!");
        BeanDelegate delegate = (BeanDelegate) MethodDelegate.create(
                bean, "getValue", BeanDelegate.class);
        assertEquals("Hello world!", delegate.getValueFromDelegate());
    }
}
