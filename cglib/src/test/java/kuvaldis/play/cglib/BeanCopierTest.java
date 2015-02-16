package kuvaldis.play.cglib;

import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.core.Converter;
import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.assertEquals;

public class BeanCopierTest {

    @Test
    public void testBeanCopier() throws Exception {
        final BeanCopier beanCopier = BeanCopier.create(SampleBean.class, OtherSampleBean.class, false);
        final SampleBean bean = new SampleBean();
        bean.setValue("Hello, world!");
        final OtherSampleBean otherBean = new OtherSampleBean();
        beanCopier.copy(bean, otherBean, null);
        assertEquals("Hello, world!", otherBean.getValue());
    }

    @Test
    public void testBeanCopierConverter() throws Exception {
        final BeanCopier beanCopier = BeanCopier.create(SampleBean.class, OtherSampleBean.class, true);
        final SampleBean bean = new SampleBean();
        bean.setValue("Hello, world!");
        final OtherSampleBean otherBean = new OtherSampleBean();
        Converter converter = (o1, o2, o3) -> "Hello, converter!";
        beanCopier.copy(bean, otherBean, converter);
        assertEquals("Hello, converter!", otherBean.getValue());
        otherBean.setValue("Hello, everybody!");
        beanCopier.copy(bean, otherBean, converter);
        assertEquals("Hello, converter!", otherBean.getValue());
    }
}
