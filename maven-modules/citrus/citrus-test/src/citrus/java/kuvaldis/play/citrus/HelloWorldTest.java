package kuvaldis.play.citrus;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.annotations.CitrusXmlTest;
import com.consol.citrus.dsl.JUnit4CitrusTestBuilder;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.message.RawMessage;
import org.junit.Test;
import org.springframework.http.HttpMethod;

import java.time.LocalDate;
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

    // run kuvaldis.play.citrus.HttpServer before
    @Test
    @CitrusTest(name = "SendMessageTest")
    public void testSendHttpClientMessageTest() throws Exception {

        description("Send Message Basic Test");

        variables().add("name", "Http");

        parallel(
                send("helloHttpClient").header("Header-Name", "${name}"),
                sequential(receive("helloHttpServer").http().uri("/greet").method(HttpMethod.GET),
                        send("helloHttpServer").payload("Hello")),
                receive("helloHttpClient") /* this is validation actually*/.messageType(MessageType.PLAINTEXT).payload("Hello, Http!")
        );
    }

    @Test
    @CitrusTest(name = "GlobalVariablesTest")
    public void testGlobalVariables() throws Exception {
        echo("This is ${projectName} project by ${userName}");
        doFinally(echo("We are not done here yet"));
    }
}
