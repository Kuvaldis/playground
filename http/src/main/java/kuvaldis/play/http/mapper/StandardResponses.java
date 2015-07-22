package kuvaldis.play.http.mapper;

import kuvaldis.play.http.Response;

public final class StandardResponses {

    private StandardResponses() {}

    public static final Response BAD_REQUEST = new Response(ResponseCode.BAD_REQUEST, "text/html", "" +
            "<html>" +
            "   <head><title>Bad Request</title></head>" +
            "   <body>Bad Request</body>" +
            "</html>");
    public static final Response NOT_FOUND = new Response(ResponseCode.NOT_FOUND, "text/html", "" +
            "<html>" +
            "   <head><title>Not Found</title></head>" +
            "   <body>Not found</body>" +
            "</html>");
    public static final Response INTERNAL_ERROR = new Response(ResponseCode.INTERNAL_ERROR, "text/html", "" +
            "<html>" +
            "   <head><title>Internal Server Error</title></head>" +
            "   <body>Internal Server Error</body>" +
            "</html>");
}
