package kuvaldis.play.http.mapper.processor;

import kuvaldis.play.http.Request;
import kuvaldis.play.http.Response;
import kuvaldis.play.http.mapper.ResponseCode;

public class RootProcessor implements UriProcessor {
    @Override
    public Response process(final Request request) {
        return new Response(ResponseCode.OK, "text/html", "" +
                "<html>" +
                "<head>" +
                "    <title>Home</title>" +
                "</head>" +
                "<body>" +
                "    Hi there! This is the home page!" +
                "</body>" +
                "</html>");
    }
}
