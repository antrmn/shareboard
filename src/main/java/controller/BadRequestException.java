package controller;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class BadRequestException extends HttpGenericException{

    private final List<String> errors;

    public BadRequestException(String message, List<String> errors, String location){
        super(message, HttpServletResponse.SC_BAD_REQUEST, location);
        this.errors = errors;
    }

    public List<String> getErrors(){return this.errors;}
}
