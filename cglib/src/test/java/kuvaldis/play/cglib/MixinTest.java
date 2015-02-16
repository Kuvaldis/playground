package kuvaldis.play.cglib;

import net.sf.cglib.proxy.Mixin;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MixinTest {

    @Test
    public void testMixin() throws Exception {
        final Mixin mixin = Mixin.create(
                new Class[]{Interface1.class, Interface2.class, MixinInterface.class},
                new Object[]{new Class1(), new Class2()});
        final MixinInterface mixinInterface = (MixinInterface) mixin;
        assertEquals("first", mixinInterface.first());
        assertEquals("second", mixinInterface.second());
    }
}
