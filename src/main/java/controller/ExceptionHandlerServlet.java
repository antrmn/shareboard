package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/error")
public class ExceptionHandlerServlet  extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("noooooooooooooo");
        HttpGenericException e = (HttpGenericException) req.getAttribute("javax.servlet.error.exception");
        req.setAttribute("exception", e);
        resp.setStatus(e.getStatusCode());
        System.out.println(getServletContext().getContextPath() + e.getLocation());
        req.getRequestDispatcher(e.getLocation()).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
