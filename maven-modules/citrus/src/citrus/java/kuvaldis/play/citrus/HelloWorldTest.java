package kuvaldis.play.citrus;

import com.consol.citrus.TestCaseMetaInfo;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.annotations.CitrusXmlTest;
import com.consol.citrus.dsl.JUnit4CitrusTestBuilder;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static com.consol.citrus.TestCaseMetaInfo.Status.FINAL;

/**
 * This is a sample Citrus integration test for loading XML syntax test case.
 *
 * @author Citrus
 */
public class HelloWorldTest extends JUnit4CitrusTestBuilder {

    @Test
    @CitrusTest(name = "HelloWorldJavaTest")
    public void testHelloWorldJava() {
        author("Kuvaldis");
        creationDate(Date.from(LocalDate.of(2015, 7, 10).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        description("Hello World Test Case!");
        status(FINAL);
        variable("text", "Hello, World!");
        echo("${text}");
    }

    @Test
    @CitrusXmlTest(name = "HelloWorldXmlTest")
    public void testHelloWorldXml() {
    }

    @Test
    @CitrusXmlTest(name = "GroovyScriptVariablesTest")
    public void testGroovyScript() throws Exception {
    }

    @Test
    @CitrusTest(name = "GlobalVariablesTest")
    public void testGlobalVariables() throws Exception {
        echo("This is ${projectName} project by ${userName}");
        doFinally(echo("We are not done here yet"));
    }
}
