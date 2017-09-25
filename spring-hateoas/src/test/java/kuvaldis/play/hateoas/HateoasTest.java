package kuvaldis.play.hateoas;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkDiscoverer;
import org.springframework.hateoas.hal.HalLinkDiscoverer;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class HateoasTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testRequestWorks() throws Exception {
        final ResultActions result = mockMvc.perform(get("/hateoas/ping"));
        result.andExpect(MockMvcResultMatchers.content().string("pong"));
    }

    @Test
    public void testLink() throws Exception {
        final Link selfLink = new Link("http://localhost:8080/something");
        assertThat(selfLink.getHref(), is("http://localhost:8080/something"));
        assertThat(selfLink.getRel(), is(Link.REL_SELF));

        final Link myRelLink = new Link("http://localhost:8080/something", "my-rel");
        assertThat(myRelLink.getHref(), is("http://localhost:8080/something"));
        assertThat(myRelLink.getRel(), is("my-rel"));
    }

    @Test
    public void testResourceLink() throws Exception {
        final PersonResource resource = new PersonResource();
        resource.firstname = "Vasilii";
        resource.lastname = "Pupkin";
        resource.add(new Link("http://myhost/vasilii_pup"));

        final Link link = new Link("http://myhost/vasilii_pup");
        assertThat(resource.getId(), is(link));
        assertThat(resource.getLink(Link.REL_SELF), is(link));
    }

    @Test
    public void testControllerLinkBuilder() throws Exception {
        final ResultActions result = mockMvc.perform(get("/hateoas/builder-test"));
        result.andExpect(status().isOk())
                .andExpect(jsonPath("href", is("http://localhost/people")))
                .andExpect(jsonPath("rel", is("people-but-not-quite")));
    }

    @Test
    public void testControllerLinkBuilder2() throws Exception {
        final ResultActions result = mockMvc.perform(get("/hateoas/builder-test-2"));
        result.andExpect(status().isOk())
                .andExpect(jsonPath("href", is("http://localhost/people/100500")))
                .andExpect(jsonPath("rel", is(Link.REL_SELF)));
    }

    @Test
    public void testControllerLinkBuilder3() throws Exception {
        final ResultActions result = mockMvc.perform(get("/hateoas/builder-test-3"));
        result.andExpect(status().isOk())
                .andExpect(jsonPath("href", is("http://localhost/people/321")))
                .andExpect(jsonPath("rel", is(Link.REL_SELF)));
    }

    @Test
    public void testControllerLinkBuilderMethod() throws Exception {
        final ResultActions result = mockMvc.perform(get("/hateoas/builder-method-test"));
        result.andExpect(status().isOk())
                .andExpect(jsonPath("href", is("http://localhost/people/126")))
                .andExpect(jsonPath("rel", is(Link.REL_SELF)));
    }

    @Test
    public void testEntityLink() throws Exception {
        final ResultActions result = mockMvc.perform(get("/hateoas/entity-link-test"));
        result.andExpect(status().isOk())
                .andExpect(jsonPath("href", is("http://localhost/orders/231")))
                .andExpect(jsonPath("rel", is(Link.REL_SELF)));
    }

    @Test
    public void testResourceAssembler() throws Exception {
        final ResultActions result = mockMvc.perform(get("/hateoas/resource-assembler-test"));
        result.andExpect(status().isOk())
                .andExpect(jsonPath("href", is("http://localhost/people/5")))
                .andExpect(jsonPath("rel", is(Link.REL_SELF)));
    }

    @Test
    public void testHalLinkDiscoverer() throws Exception {
        final String content = "{'_links': {'foo': {'href': '/foo/bar'}}}";
        final LinkDiscoverer discoverer = new HalLinkDiscoverer();
        final Link link = discoverer.findLinkWithRel("foo", content);
        assertThat(link.getRel(), is("foo"));
        assertThat(link.getHref(), is("/foo/bar"));
    }
}