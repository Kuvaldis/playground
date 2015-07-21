package kuvaldis.play.http;

public class Request {

    private final String method;
    private final String uri;
    private final String host;

    public Request(String method, String uri, String host) {
        this.method = method;
        this.uri = uri;
        this.host = host;
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getHost() {
        return host;
    }
}
