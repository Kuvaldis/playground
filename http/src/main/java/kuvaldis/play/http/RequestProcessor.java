package kuvaldis.play.http;

import kuvaldis.play.http.mapper.ResponseCode;
import kuvaldis.play.http.mapper.UriProcessor;
import kuvaldis.play.http.mapper.UriProcessorMap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Optional;

public class RequestProcessor {

    private final BufferedReader reader;
    private final BufferedWriter writer;

    private static final Response BAD_REQUEST = new Response(ResponseCode.BAD_REQUEST);
    private static final Response NOT_FOUND = new Response(ResponseCode.NOT_FOUND);
    private static final Response INTERNAL_ERROR = new Response(ResponseCode.INTERNAL_ERROR);

    public RequestProcessor(BufferedReader reader, BufferedWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public void process() throws Exception {
        System.out.println("Process request...");
        try {
            final Optional<Request> dataOptional = parseRequest(reader);
            final Response response = dataOptional.map(this::processRequest).orElse(BAD_REQUEST);
            writeResponse(response);
        } catch (final Exception e) {
            e.printStackTrace();
            writeResponse(INTERNAL_ERROR);
        } finally {
            writer.flush();
            System.out.println("Process request is done...");
        }
    }

    private Optional<Request> parseRequest(final BufferedReader reader) throws IOException {
        final String requestLine = reader.readLine();
        if (requestLine == null || requestLine.isEmpty()) {
            return Optional.empty();
        }
        //        todo read other stuff
//        String s;
//        while ((s = reader.readLine()) != null && !s.isEmpty()) {
//            writer.append(s);
//        }
        return collectRequest(requestLine);
    }

    private Optional<Request> collectRequest(final String requestLine) {
        final String[] requestLineParts = requestLine.split(" ");
        return Optional.of(new Request(requestLineParts[0], requestLineParts[1], null));
    }

    private Response processRequest(final Request request) {
        return UriProcessorMap.getProcessor(request.getUri())
                .map((processor) -> processor.process(request))
                .orElse(NOT_FOUND);
    }

    private void writeResponse(final Response response) throws IOException {
        writer.write("HTTP/1.1 " + response.getStatus().getCode() + " " + response.getStatus().getDescription());
        writer.newLine();
//        writer.write("HTTP/1.1 200 OK\r\n");
        writer.write("Date: Fri, 31 Dec 1999 23:59:59 GMT\r\n");
        writer.write("Server: Apache/0.8.4\r\n");
        writer.write("Content-Type: text/html\r\n");
        writer.write("Content-Length: 0\r\n");
        writer.write("Expires: Sat, 01 Jan 2000 00:59:59 GMT\r\n");
        writer.write("Last-modified: Fri, 09 Aug 1996 14:21:40 GMT\r\n");
        writer.write("\r\n");
    }
}
