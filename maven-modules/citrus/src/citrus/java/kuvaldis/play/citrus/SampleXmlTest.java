package kuvaldis.play.citrus;

import com.consol.citrus.annotations.CitrusXmlTest;
import com.consol.citrus.dsl.JUnit4CitrusTestBuilder;
import org.junit.Test;

/**
 * This is a sample Citrus integration test for loading XML syntax test case.
 *
 * @author Citrus
 */
public class SampleXmlTest extends JUnit4CitrusTestBuilder {

    @Test
    @CitrusXmlTest(name = "SampleXmlTest")
    public void sampleXmlTest() {
    }
}
