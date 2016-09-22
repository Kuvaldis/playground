package kuvaldis.play.java;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.osjava.sj.SimpleContext;
import org.osjava.sj.SimpleContextFactory;
import org.osjava.sj.memory.MemoryContextFactory;

import javax.naming.Context;
import javax.naming.InitialContext;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class JNDI {

    private static final String JNDI_ROOT_DIR_NAME = "jndiRoot";

    @Before
    public void setUp() throws Exception {
        // in memory context will be used
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, SimpleContextFactory.class.getName());
        final File jndiRoot = new File(JNDI_ROOT_DIR_NAME);
        jndiRoot.mkdir();
        System.setProperty(SimpleContext.SIMPLE_ROOT, jndiRoot.getAbsolutePath());
    }

    @After
    public void tearDown() throws Exception {
        final File jndiRoot = new File(JNDI_ROOT_DIR_NAME);
        jndiRoot.delete();
    }

    @Test
    public void testSimpleJndi() throws Exception {
        final Context context = new InitialContext();
        context.bind("/config/applicationName", "MyApp");

        // in another application
        final String appName = (String) context.lookup("/config/applicationName");
        assertEquals("MyApp", appName);
    }
}
