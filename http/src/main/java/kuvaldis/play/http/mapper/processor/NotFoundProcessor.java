package kuvaldis.play.http.mapper.processor;

import kuvaldis.play.http.Request;
import kuvaldis.play.http.Response;

import static kuvaldis.play.http.mapper.StandardResponses.NOT_FOUND;

public class NotFoundProcessor implements UriProcessor {
    @Override
    public Response process(final Request request) {
        return NOT_FOUND;
    }
}
