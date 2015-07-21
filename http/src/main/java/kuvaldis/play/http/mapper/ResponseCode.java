package kuvaldis.play.http.mapper;

import kuvaldis.play.http.Response;

public final class ResponseCode {

    private Integer code;
    private String description;

    private ResponseCode(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public static final ResponseCode BAD_REQUEST = new ResponseCode(400, "Bad Request");
    public static final ResponseCode NOT_FOUND = new ResponseCode(404, "Not Found");
    public static final ResponseCode INTERNAL_ERROR = new ResponseCode(500, "Internal Server Error");

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
