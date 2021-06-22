package controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public final class ErrorForwarder {
    public static void sendError(HttpServletRequest req, HttpServletResponse res, List<String> messages,
                                 int statusCode, String location) throws ServletException, IOException {
        res.setStatus(statusCode);
        req.setAttribute("errors", messages);
        req.getRequestDispatcher(location).forward(req, res);
    }
}
