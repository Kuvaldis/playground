package kuvaldis.play.restdoc;

import org.hibernate.validator.internal.metadata.raw.ConstrainedField;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.hypermedia.HypermediaDocumentation;
import org.springframework.restdocs.hypermedia.LinkDescriptor;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class RestDocTest {

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("build/generated-snippets");

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .build();
    }

    @Test
    public void testRequestWorks() throws Exception {
        final ResultActions result = mockMvc.perform(get("/restdoc/ping"));
        result.andExpect(content().string("pong"));
    }

    @Test
    public void testSimple() throws Exception {
        mockMvc.perform(get("/restdoc/simple-test").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("simple-test"));
    }

    @Test
    public void testHypermediaLinks() throws Exception {
        mockMvc.perform(get("/restdoc/hypermedia-links").accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("hypermedia-links", HypermediaDocumentation.links(HypermediaDocumentation.halLinks(),
                        HypermediaDocumentation.linkWithRel("alpha").description("Link to the alpha resource"),
                        HypermediaDocumentation.linkWithRel("bravo").description("Link to the bravo resource"),
                        HypermediaDocumentation.linkWithRel("random").description("Random link").optional()
                )));
    }

    @Test
    public void testResponseFields() throws Exception {
        mockMvc.perform(get("/restdoc/response-fields").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("response-fields",
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("contact.email")
                                        .type(JsonFieldType.STRING)
                                        .description("The user's email address"),
                                PayloadDocumentation.fieldWithPath("contact.name")
                                        .description("The user's name"))));
    }

    @Test
    public void testPathParams() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.get("/restdoc/path-params/{first}/{second}", "hi", "there")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("path-params", RequestDocumentation.pathParameters(
                        RequestDocumentation.parameterWithName("first").description("First parameter"),
                        RequestDocumentation.parameterWithName("second").description("Second parameter")
                )));
    }

    static class UserInput {

        @NotNull
        @Size(min = 1)
        String name;

        @NotNull
        @Size(min = 8)
        String password;

    }

    @Test
    public void testConstrainedFields() throws Exception {
        // see https://github.com/spring-projects/spring-restdocs/blob/v1.2.1.RELEASE/samples/rest-notes-spring-hateoas/src/test/java/com/example/notes/ApiDocumentation.java
        final ConstraintDescriptions userConstraints = new ConstraintDescriptions(UserInput.class);
        final List<String> descriptions = userConstraints.descriptionsForProperty("name");
        System.out.println(descriptions);
    }
}
