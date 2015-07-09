package kuvaldis.play.citrus;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.dsl.JUnit4CitrusTestBuilder;
import org.junit.Test;

/**
 * This is a sample Citrus integration test for loading XML syntax test case.
 *
 * @author Citrus
 */
public class SampleJavaTest extends JUnit4CitrusTestBuilder {

    @Test
    @CitrusTest(name = "SampleJavaTest")
    public void sampleTest() {
        variable("now", "citrus:currentDate()");

        echo("Today is: ${now}");
    }
}
