package controller;

public class HttpGenericException extends RuntimeException{
    private final String location;
    private final int statusCode;

    public HttpGenericException(String message, int statusCode, String location){
        super(message);
        this.statusCode = statusCode;
        this.location = location;
    }

    public HttpGenericException(int statusCode, String location){
        super();
        this.statusCode = statusCode;
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
