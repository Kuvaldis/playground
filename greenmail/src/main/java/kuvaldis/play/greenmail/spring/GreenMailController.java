package kuvaldis.play.greenmail.spring;

import com.icegreen.greenmail.spring.GreenMailBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@RestController
public class GreenMailController {

    @Autowired
    private GreenMailBean greenMailBean;

    private static class MailData {
        private final String from;
        private final String to;
        private final String subject;
        private final Object content;

        public MailData(String from, String to, String subject, Object content) {
            this.from = from;
            this.to = to;
            this.subject = subject;
            this.content = content;
        }

        public String getFrom() {
            return from;
        }

        public String getTo() {
            return to;
        }

        public String getSubject() {
            return subject;
        }

        public Object getContent() {
            return content;
        }
    }

    @RequestMapping("/")
    public List<MailData> home() {
        return Arrays.asList(greenMailBean.getReceivedMessages()).stream()
                .map(this::convert)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private MailData convert(final MimeMessage message) {
        try {
            return new MailData(
                    convertAddress(message.getFrom()),
                    convertAddress(message.getAllRecipients()),
                    message.getSubject(),
                    message.getContent()
            );
        } catch (IOException | MessagingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String convertAddress(Address[] addresses) throws MessagingException {
        return String.join(",", Arrays.asList(addresses).stream().map(Address::toString).collect(Collectors.toList()));
    }

    @RequestMapping("/send")
    public void send(final @RequestParam("to") String to, final @RequestParam("from") String from) {
        greenMailBean.sendEmail(to, from, String.valueOf(ThreadLocalRandom.current().nextLong()), "Message Body");
    }
}
