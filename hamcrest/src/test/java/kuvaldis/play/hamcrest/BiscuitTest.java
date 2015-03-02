package kuvaldis.play.hamcrest;

import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

// XmlConfigurator
// xml with matchers list to generate from. Example: /home/kuvaldis/Work/Mine/Study/playground/hamcrest/src/test/resources/kuvaldis/play/hamcrest/matchers.xml
// sources of matcher classes /home/kuvaldis/Work/Mine/Study/playground/hamcrest/src/test/java/kuvaldis/play/hamcrest
// sugar class to generate kuvaldis.play.hamcrest.CustomMatchers
// sugar class to put to /home/kuvaldis/Work/Mine/Study/playground/hamcrest/src/test/java
public class BiscuitTest {

    @Test
    public void testEquals() throws Exception {
        assertThat("Ginger", equalTo("Ginger"));
        //noinspection UnnecessaryBoxing
        assertThat("Integer equality", 1, equalTo(new Integer(1)));
        assertThat("Ginger", is(equalTo("Ginger")));
    }

    @Test
    public void testCore() throws Exception {
        assertThat("Anything", is(anything()));
        assertThat("Cake", describedAs("It can lie and it is", is("Cake"))); // description for failure
    }

    @Test
    public void testLogical() throws Exception {
        assertThat("Bread", is(allOf(notNullValue(), isA(String.class), not("Butter"), not("Pitt"))));
        assertThat("Tank", is(anyOf(isA(String.class), nullValue())));
    }

    @Test
    public void testObject() throws Exception {
        assertThat("Me", is(sameInstance("Me")));
        //noinspection RedundantStringConstructorCall
        assertThat("Me", is(not(sameInstance(new String("Me")))));
        assertThat("To the Moon", hasToString("To the Moon"));
    }

    @Test
    public void testText() throws Exception {
        assertThat("Knight", containsString("night"));
        assertThat("", Matchers.isEmptyString());
        assertThat("", Matchers.isEmptyOrNullString());
        assertThat("AmUsiNg StrIng", Matchers.equalToIgnoringCase("AmusIng sTrIng"));
    }

    @Test
    public void testCollections() throws Exception {
        final List<String> list = Arrays.asList("ABC-book", "The second one", "The blue one");
        assertThat(list, hasItems("The second one"));
        assertThat(list.toArray(), hasItemInArray("The second one"));
        assertThat(list, Matchers.containsInAnyOrder("The second one", "ABC-book", "The blue one")); // all the values
        assertThat("The second one", isIn(list));

        final Map<String, String> map = new HashMap<String, String>() {{
            put("Pizza", "Mario");
            put("Pasta", "Luigi");
        }};
        assertThat(map, hasKey("Pizza"));
        assertThat(map, Matchers.hasValue("Luigi"));
        assertThat(map, Matchers.hasEntry("Pizza", "Mario"));
    }

    @Test
    public void testBean() throws Exception {
        assertThat(new Bean(), Matchers.hasProperty("property"));
    }

    @Test
    public void testNumber() throws Exception {
        assertThat(2.02134, closeTo(2.0, 0.1));
        assertThat(3, greaterThan(2));
    }

    @Test
    public void testCustom() throws Exception {
        assertThat(Math.sqrt(-1), is(CustomMatchers.notANumber()));
    }
}
