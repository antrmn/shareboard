package controller;

import javax.servlet.http.HttpServletResponse;

public class ForbiddenException extends HttpGenericException{

    public ForbiddenException(String message, String location) {
        super(message, HttpServletResponse.SC_FORBIDDEN, location);
    }

    public ForbiddenException(String message) {
        super(message, HttpServletResponse.SC_FORBIDDEN, "booooh");
    }
}
