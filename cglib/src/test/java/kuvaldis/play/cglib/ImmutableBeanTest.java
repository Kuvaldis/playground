package kuvaldis.play.cglib;

import net.sf.cglib.beans.ImmutableBean;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class ImmutableBeanTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testImmutableBean() throws Exception {
        final SampleBean bean = new SampleBean();
        bean.setValue("Hello, world!");
        final SampleBean immutableBean = (SampleBean) ImmutableBean.create(bean);
        assertEquals("Hello, world!", immutableBean.getValue());
        bean.setValue("Hello, world, again!");
        assertEquals("Hello, world, again!", bean.getValue());
        expectedException.expect(IllegalStateException.class);
        immutableBean.setValue("Hello, immutable!");
    }
}
