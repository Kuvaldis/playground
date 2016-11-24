package kuvaldis.play.springframework;

import java.util.List;
import java.util.Properties;

public class EmailsStorageBean {

    private Properties emails;

    public Properties getEmails() {
        return emails;
    }

    public void setEmails(Properties emails) {
        this.emails = emails;
    }
}
