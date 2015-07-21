package kuvaldis.play.http.mapper;

import kuvaldis.play.http.Request;
import kuvaldis.play.http.Response;

public interface UriProcessor {
    Response process(Request request);
}
