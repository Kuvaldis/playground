package kuvaldis.play.http;

import kuvaldis.play.http.mapper.ResponseCode;

public final class Response {
    private final ResponseCode status;
    private final String contentType;
    private final String body;

    public Response(ResponseCode status, String contentType, String body) {
        this.status = status;
        this.contentType = contentType;
        this.body = body;
    }

    public ResponseCode getStatus() {
        return status;
    }

    public String getContentType() {
        return contentType;
    }

    public String getBody() {
        return body;
    }
}
