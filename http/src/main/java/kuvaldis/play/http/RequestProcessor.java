package kuvaldis.play.http;

import kuvaldis.play.http.mapper.UriProcessorMap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Locale;
import java.util.Optional;

import static kuvaldis.play.http.mapper.StandardResponses.*;

public class RequestProcessor {

    private final BufferedReader reader;
    private final BufferedWriter writer;

    public RequestProcessor(BufferedReader reader, BufferedWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public void process() throws Exception {
        System.out.println("Process request...");
        try {
            final Optional<Request> requestOptional = parseRequest(reader);
            final Response response = requestOptional.map(this::processRequest).orElse(BAD_REQUEST);
            logResult(requestOptional, response);
            writeResponse(response);
        } catch (final Exception e) {
            e.printStackTrace();
            writeResponse(INTERNAL_ERROR);
        } finally {
            writer.flush();
            System.out.println("Process request is done...");
        }
    }

    private void logResult(final Optional<Request> requestOptional, final Response response) {
        System.out.println(requestOptional.map((request) -> request.getMethod() + " request to " +
                request.getUri() + " from " + request.getHost())
                .orElse("Empty request") + " processed with response status " + response.getStatus().getCode());
    }

    private Optional<Request> parseRequest(final BufferedReader reader) throws IOException {
        final String requestLine = reader.readLine();
        if (requestLine == null || requestLine.isEmpty()) {
            return Optional.empty();
        }
        //        todo read other stuff like headers or body
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
        writer.write("Date: " + new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zz", Locale.ENGLISH).format(Instant.now().toEpochMilli()));
        writer.newLine();
        writer.write("Server: Test/0.0.1");
        writer.newLine();
        writer.write("Content-Type: " + response.getContentType());
        writer.newLine();
        writer.write("Content-Length: " + response.getBody().getBytes("UTF-8").length);
        writer.newLine();
        writer.newLine();
        writer.write(response.getBody());
    }
}
