package kuvaldis.play.springboot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest(randomPort = true)
public class ApplicationTest {

    @Value("${local.server.port}")
    private String port;

    @Test
    public void testAddPerson() throws Exception {
        final RestTemplate template = new RestTemplate();
        final List<Person> people = Arrays.asList(
                template.getForEntity("http://localhost:" + port + "/people/add", Person[].class).getBody());
        assertEquals(1, people.size());
    }
}