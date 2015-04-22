package kuvaldis.play.picocontainer;

import org.junit.Test;
import org.picocontainer.*;
import org.picocontainer.behaviors.*;
import org.picocontainer.composers.RegexComposer;
import org.picocontainer.containers.PropertiesPicoContainer;
import org.picocontainer.injectors.AnnotatedFieldInjection;
import org.picocontainer.injectors.ConstructorInjection;
import org.picocontainer.injectors.ProviderAdapter;
import org.picocontainer.injectors.SetterInjection;
import org.picocontainer.monitors.AbstractComponentMonitor;
import org.picocontainer.monitors.ComposingMonitor;
import org.picocontainer.parameters.CollectionComponentParameter;
import org.picocontainer.parameters.ComponentParameter;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.util.Arrays.asList;
import static java.util.Collections.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.verify;
import static org.picocontainer.Characteristics.USE_NAMES;

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

    @Test
    public void testCaching() throws Exception {
        final MutablePicoContainer picoContainer = new DefaultPicoContainer();
        picoContainer.addComponent(Apple.class);
        final Apple apple1 = picoContainer.getComponent(Apple.class);
        final Apple apple2 = picoContainer.getComponent(Apple.class);
        assertNotSame(apple1, apple2);

        final MutablePicoContainer cachingPicoContainer = new DefaultPicoContainer(new Caching());
        cachingPicoContainer.addComponent(Apple.class);
        final Apple apple3 = cachingPicoContainer.getComponent(Apple.class);
        final Apple apple4 = cachingPicoContainer.getComponent(Apple.class);
        assertSame(apple3, apple4);

        final MutablePicoContainer optinCachingPicoContainer = new DefaultPicoContainer(new OptInCaching());
        optinCachingPicoContainer
                .as(Characteristics.CACHE).addComponent(Apple.class)
                .addComponent(Orange.class);
        final Apple apple5 = optinCachingPicoContainer.getComponent(Apple.class);
        final Apple apple6 = optinCachingPicoContainer.getComponent(Apple.class);
        assertSame(apple5, apple6);
        final Orange orange1 = optinCachingPicoContainer.getComponent(Orange.class);
        final Orange orange2 = optinCachingPicoContainer.getComponent(Orange.class);
        assertNotSame(orange1, orange2);

        final MutablePicoContainer threadCachingPicoContainer = new DefaultPicoContainer(new ThreadCaching());
        threadCachingPicoContainer.addComponent(Apple.class);
        final Future<List<Apple>> future1 = Executors.newSingleThreadExecutor().submit(() ->
                Arrays.asList(threadCachingPicoContainer.getComponent(Apple.class),
                        threadCachingPicoContainer.getComponent(Apple.class)));
        final List<Apple> apples1 = future1.get();
        final Apple apple7 = apples1.get(0);
        final Apple apple8 = apples1.get(1);
        final Future<List<Apple>> future2 = Executors.newSingleThreadExecutor().submit(() ->
                singletonList(threadCachingPicoContainer.getComponent(Apple.class)));
        final Apple apple9 = future2.get().get(0);
        assertSame(apple7, apple8);
        assertNotSame(apple7, apple9);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testImplementationHiding() throws Exception {
        final MutablePicoContainer implementationHidingPicoContainer = new DefaultPicoContainer(new ImplementationHiding());
        final Map instance = mock(Map.class);
        implementationHidingPicoContainer.addComponent(Map.class, instance);
        final Map map = implementationHidingPicoContainer.getComponent(Map.class);
        map.put(any(), any());
        verify(instance).put(any(), any());
    }

    @Test
    public void testInterception() throws Exception {
        final MutablePicoContainer interceptingPicoContainer = new DefaultPicoContainer(new Intercepting());
        interceptingPicoContainer.addComponent(Service.class, ServiceImpl.class);
        final Intercepted intercepted = interceptingPicoContainer
                .getComponentAdapter(Service.class).findAdapterOfType(Intercepted.class);
        intercepted.addPostInvocation(Service.class, new ServiceInterceptor(intercepted.getController()));
        final Service service = interceptingPicoContainer.getComponent(Service.class);
        assertEquals("Intercepted: ServiceImpl.call", service.call());
    }

    @Test
    public void testArraysInject() throws Exception {
        final Sea sea = new DefaultPicoContainer()
                .addComponent(Cod.class)
                .addComponent("Shark", Shark.class)
                .addComponent(Sea.class, Sea.class,
                        new CollectionComponentParameter(Fish.class, false) {
                            @Override
                            protected boolean evaluate(ComponentAdapter adapter) {
                                //noinspection EqualsBetweenInconvertibleTypes
                                return Cod.class.equals(adapter.getComponentImplementation());
                            }
                        },
                        new ComponentParameter(Cod.class, false),
                        new ComponentParameter(Shark.class, false))
                .getComponent(Sea.class);
        assertArrayEquals(new Fish[]{new Cod()}, sea.getFishes());
        assertEquals(singletonList(new Cod()), sea.getCods());
        assertEquals(singletonMap("Shark", new Shark()), sea.getSharks());
    }

    @Test
    public void testParameterNames() throws Exception {
        final Sea2 sea = new DefaultPicoContainer()
                .addComponent("cod", Cod.class)
                .addComponent("shark", Shark.class)
                .as(USE_NAMES).addComponent(Sea2.class)
                .getComponent(Sea2.class);
        assertEquals(new Cod(), sea.getCod());
        assertEquals(new Shark(), sea.getShark());
    }

    @Test
    public void testConfig() throws Exception {
        final Service service = new DefaultPicoContainer()
                .addComponent(Service.class, ServiceWithConfig.class)
                .addConfig("dataBaseUrl", "someUrl")
                .getComponent(Service.class);
        assertEquals("someUrl", service.call());
    }

    @Test
    public void testComponentMonitor() throws Exception {
        final List<String> checker = new ArrayList<>();
        final MutablePicoContainer picoContainer = new DefaultPicoContainer(new AbstractComponentMonitor() {
            @Override
            public <T> Constructor<T> instantiating(PicoContainer container, ComponentAdapter<T> componentAdapter, Constructor<T> constructor) {
                checker.add("instantiating");
                return super.instantiating(container, componentAdapter, constructor);
            }
        }).addComponent(Service.class, ServiceImpl.class);
        picoContainer.getComponent(Service.class).call();
        assertEquals(singletonList("instantiating"), checker);
    }

    @Test
    public void testRegexComposer() throws Exception {
        final MutablePicoContainer picoContainer = new DefaultPicoContainer(new ComposingMonitor(new RegexComposer()));
        picoContainer.addComponent("shark1", Shark.class)
                .addComponent("shark2", Shark.class);
        //noinspection unchecked
        final List<Shark> apples = (List<Shark>) picoContainer.getComponent("shark[1-9]");
        assertEquals(asList(new Shark(), new Shark()), apples);
    }

    @Test
    public void testPropertiesContainer() throws Exception {
        final Properties properties = new Properties();
        properties.setProperty("dataBaseUrl", "someUrl");
        final MutablePicoContainer picoContainer = new DefaultPicoContainer(new PropertiesPicoContainer(properties));
        picoContainer.addComponent(Service.class, ServiceWithConfig.class);
        final Service service = picoContainer.getComponent(Service.class);
        assertEquals("someUrl", service.call());
    }
}
