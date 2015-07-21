package kuvaldis.play.http;

import kuvaldis.play.http.mapper.ResponseCode;

public final class Response {
    private final ResponseCode status;

    public Response(ResponseCode status) {
        this.status = status;
    }

    public ResponseCode getStatus() {
        return status;
    }
}
