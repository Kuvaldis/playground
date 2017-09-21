package kuvaldis.play.hateoas;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@WebMvcTest
public class HateoasTestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testRequestWorks() throws Exception {
        final ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/hateoas/ping"));
        result.andExpect(MockMvcResultMatchers.content().string("pong"));
    }
}