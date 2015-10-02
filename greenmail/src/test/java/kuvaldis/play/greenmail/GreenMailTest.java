package kuvaldis.play.greenmail;

import com.icegreen.greenmail.junit.GreenMailRule;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GreenMailTest {

    @Rule
    public final GreenMailRule greenMail = new GreenMailRule(ServerSetupTest.SMTP);

    @Test
    public void testSend() throws MessagingException {
        GreenMailUtil.sendTextEmailTest("to@localhost.com", "from@localhost.com", GreenMailUtil.random(), "some body");
        assertEquals("some body", GreenMailUtil.getBody(greenMail.getReceivedMessages()[0]));
    }
}
