package kuvaldis.play.restdoc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@WebMvcTest
public class RestDocTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testRequestWorks() throws Exception {
        final ResultActions result = mockMvc.perform(get("/restdoc/ping"));
        result.andExpect(content().string("pong"));
    }

}
