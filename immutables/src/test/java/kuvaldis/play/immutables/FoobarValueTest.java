package kuvaldis.play.immutables;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class FoobarValueTest {

    @Test
    public void testHelloWorld() throws Exception {
        // given
        final ImmutableFoobarValue.Builder builder = ImmutableFoobarValue.builder()
                .foo(2)
                .bar("Bar")
                .addBuz(1, 3, 4);

        // when
        final FoobarValue value = builder.build();

        // then
        assertEquals(2, value.foo());
        assertEquals("Bar", value.bar());
        assertEquals(Arrays.asList(1, 3, 4), value.buz());
    }

    @Test
    public void testStagedBuilder() throws Exception {
        ImmutableStaged.builder()
                // only this order is allowed, so you cannot call build before all the other fields are set
                .name("Billy Bounce")
                .age(33)
                .isEmployed(false)
                .build();
    }

    @Test
    public void testConstructorMethod() throws Exception {
        // when
        final ImmutableHostWithPort localhost = ImmutableHostWithPort.of("localhost", 8080);

        // then
        assertEquals("localhost", localhost.hostname());
        assertEquals(8080, localhost.port());
    }

    @Test
    public void testDefault() throws Exception {
        // when
        PlayerInfo veteran = ImmutablePlayerInfo.builder()
                .id(1)
                .name("Fiddler")
                .gamesPlayed(99)
                .build();

        PlayerInfo novice = ImmutablePlayerInfo.builder()
                .id(2)
                .build();

        // then
        assertEquals(1, veteran.id());
        assertEquals("Fiddler", veteran.getName());
        assertEquals(99, veteran.gamesPlayed());

        assertEquals(2, novice.id());
        assertEquals("Anonymous_2", novice.getName());
        assertEquals(0, novice.gamesPlayed());
    }

    @Test
    public void testDerivedAttributes() throws Exception {
        // when
        final Order order = ImmutableOrder.builder()
                .addItems(ImmutableItem.of("item1", 11))
                .addItems(ImmutableItem.of("item2", 22))
                .build();

        // then
        assertEquals(33, order.totalCount());
    }
}