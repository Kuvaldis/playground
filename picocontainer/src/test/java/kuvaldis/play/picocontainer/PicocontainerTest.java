package kuvaldis.play.picocontainer;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.injectors.AnnotatedFieldInjection;
import org.picocontainer.injectors.ConstructorInjection;
import org.picocontainer.injectors.ProviderAdapter;
import org.picocontainer.injectors.SetterInjection;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.Arrays;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PicocontainerTest {

    @Test
    public void testHelloWorld() throws Exception {
        final MutablePicoContainer picoContainer = new DefaultPicoContainer();
        asList(Apple.class, Juicer.class, Peeler.class)
                .stream()
                .forEach(picoContainer::addComponent);
        final Juicer juicer = picoContainer.getComponent(Juicer.class);
        assertNotNull(juicer);
        assertNotNull(juicer.getPeelable());
        assertEquals(Apple.class, juicer.getPeelable().getClass());
        assertNotNull(juicer.getPeeler());
        assertEquals(Peeler.class, juicer.getPeeler().getClass());
    }

    @Test
    public void testConstructorInjection() throws Exception {
        final MutablePicoContainer picoContainer = new DefaultPicoContainer(new ConstructorInjection());
        asList(Apple.class, Orange.class, Banana.class, FruitSalad.class)
                .stream()
                .forEach(picoContainer::addComponent);
        final FruitSalad fruitSalad = picoContainer.getComponent(FruitSalad.class);
        assertNotNull(fruitSalad.getApple());
        assertNotNull(fruitSalad.getBanana());
        assertNotNull(fruitSalad.getOrange());
    }

    @Test
    public void testSetterInjection() throws Exception {
        final MutablePicoContainer picoContainer = new DefaultPicoContainer(new SetterInjection());
        asList(Apple.class, Orange.class, Banana.class, FruitSalad2.class)
                .stream()
                .forEach(picoContainer::addComponent);
        final FruitSalad2 fruitSalad = picoContainer.getComponent(FruitSalad2.class);
        assertNotNull(fruitSalad.getApple());
        assertNotNull(fruitSalad.getBanana());
        assertNotNull(fruitSalad.getOrange());
    }

    @Test
    public void testJsr330DependencyInjection() throws Exception {
        final MutablePicoContainer picoContainer = new DefaultPicoContainer(new AnnotatedFieldInjection(Inject.class));
        asList(Apple.class, Orange.class, Banana.class, FruitSalad3.class)
                .stream()
                .forEach(picoContainer::addComponent);
        final FruitSalad3 fruitSalad = picoContainer.getComponent(FruitSalad3.class);
        assertNotNull(fruitSalad.getApple());
        assertNotNull(fruitSalad.getBanana());
        assertNotNull(fruitSalad.getOrange());
    }

    @Test
    public void testFactoryInjection() throws Exception {
        final MutablePicoContainer picoContainer = new DefaultPicoContainer();
        picoContainer.addComponent(Apple2.class);
        final Logger logger = mock(Logger.class);
        final FixedInstanceLogFactoryInjector factoryInjector = new FixedInstanceLogFactoryInjector(logger);
        picoContainer.addAdapter(factoryInjector);
        final Apple2 apple = picoContainer.getComponent(Apple2.class);
        apple.peel();
        verify(logger).info(anyString());
    }

    @Test
    public void testProviderInjection() throws Exception {
        final MutablePicoContainer picoContainer = new DefaultPicoContainer();
        asList(Apple.class, Orange.class, Banana.class)
                .stream()
                .forEach(picoContainer::addComponent);
        picoContainer.addAdapter(new ProviderAdapter(new FruitSaladProvider()));
        final FruitSalad fruitSalad = picoContainer.getComponent(FruitSalad.class);
        assertNotNull(fruitSalad);
        assertNotNull(fruitSalad.getApple());
        assertNotNull(fruitSalad.getBanana());
        assertNotNull(fruitSalad.getOrange());
    }
}
