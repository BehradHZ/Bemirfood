package HttpClientHandler;

public enum HttpRequest {
    POST, GET, PUT, DELETE, PATCH;

    @Override
    public String toString() {
        return this.name().toUpperCase();
    }
}
