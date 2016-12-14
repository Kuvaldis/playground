package kuvaldis.play.springframework;

import kuvaldis.play.springframework.qualifier.MovieRecommender;
import kuvaldis.play.springframework.scoperesolver.ScopeResolverBean;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
}
