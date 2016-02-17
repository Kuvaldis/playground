package kuvaldis.play.assertj;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class AssertJTest {

    private static class Person {
        private final String name;
        private final int age;

        private Person(final String name, final int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }

    @Test
    public void testHW() throws Exception {
        // basic test
        assertThat("aaa").contains("a");

        // test name
        final int age = 10;
        assertThat(age).as("check character's age").isEqualTo(10);

        // filtering
        final Person abo = new Person("abo", age);
        final List<Person> people = Arrays.asList(new Person("ooo", 1), new Person("aaa", 11), abo, new Person("bbb", 24));
        assertThat(people).filteredOn(p -> p.getName().contains("o"))
                .filteredOn("name", not("ooo"))
                .containsOnly(abo);

        // properties extraction, tuple
        assertThat(people).extracting("name", "age")
                .contains(tuple("ooo", 1));

        // check exception thrown
        assertThatThrownBy(() -> {
            throw new Exception("oops");
        }).isInstanceOf(Exception.class).hasMessage("oops");
    }
}
