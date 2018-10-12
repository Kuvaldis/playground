package kuvaldis.play.microprofile;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.metrics.annotation.Counted;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Counted
@Path("/books")
@RequestScoped
public class BookController {

    @Inject
    private BookService bookService;

    @Inject
    @ConfigProperty(name = "max.books.per.page", defaultValue = "20")
    int maxBooks;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        final Books books = new Books(bookService.getAll().stream()
                .limit(maxBooks)
                .collect(Collectors.toList()));
        return Response.ok(books).build();
    }
}
