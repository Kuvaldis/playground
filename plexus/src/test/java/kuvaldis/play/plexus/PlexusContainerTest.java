package kuvaldis.play.plexus;

import org.codehaus.plexus.DefaultPlexusContainer;
import org.codehaus.plexus.PlexusContainer;
import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PlexusContainerTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testParmesanCheese() throws Exception {
        expectedException.expect(UnsupportedOperationException.class);
        final PlexusContainer container = new DefaultPlexusContainer();
        final Cheese cheese = (Cheese) container.lookup(Cheese.class.getName(), "parmesan");
        assertNotNull(cheese);
        assertThat(cheese, instanceOf(ParmesanCheese.class));
        assertEquals(cheese.getAroma(), "Strong");
        cheese.slice(1);
        container.dispose();
    }

    @Test
    public void testSetField() throws Exception {
        final PlexusContainer container = new DefaultPlexusContainer();
        final Bean bean = container.lookup(Bean.class, "default");
        assertEquals(1, bean.getInteger().intValue());
    }

    @Test
    public void testInject() throws Exception {
        final PlexusContainer container = new DefaultPlexusContainer();
        final Bean delegate = container.lookup(Bean.class, "delegate");
        assertThat(delegate, instanceOf(DelegateBean.class));
        final Field beanField = delegate.getClass().getDeclaredField("bean");
        beanField.setAccessible(true);
        final Bean bean = (Bean) beanField.get(delegate);
        assertThat(bean, instanceOf(DefaultBean.class));
        final Bean defaultBean = container.lookup(Bean.class, "default");
        assertEquals(defaultBean, bean);
        assertEquals(1, delegate.getInteger().intValue());
        delegate.setInteger(2);
        assertEquals(2, delegate.getInteger().intValue());
        assertEquals(2, bean.getInteger().intValue());
        assertEquals(2, defaultBean.getInteger().intValue());
    }

    @Test
    public void testInjectCollection() throws Exception {
        final PlexusContainer container = new DefaultPlexusContainer();
        final Holder holder = container.lookup(Holder.class);
        assertNotNull(holder.getList());
        assertNotNull(holder.getMap());
        assertEquals(container.lookup(Bean.class, "default"), holder.getMap().get("default"));
        assertEquals(container.lookup(Bean.class, "delegate"), holder.getMap().get("delegate"));
        assertTrue(holder.getList().contains(container.lookup(Bean.class, "delegate")));
        assertTrue(holder.getList().contains(container.lookup(Bean.class, "default")));
    }
}