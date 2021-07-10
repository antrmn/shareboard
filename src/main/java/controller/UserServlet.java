package controller;

import controller.util.ErrorForwarder;
import model.persistence.ConPool;
import model.user.User;
import model.user.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/user")
public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("name");

        if(username == null || username.isBlank()){
            ErrorForwarder.sendError(req, resp, "Specificare un utente", 400);
            return;
        }

        try(Connection con = ConPool.getConnection()){
            UserDAO service = new UserDAO(con);
            User user = service.get(username, false);
            if(user == null){
                ErrorForwarder.sendError(req, resp, "L'utente specificato non esiste", 400);
                return;
            }
            req.setAttribute("user", user);
        } catch (SQLException throwables) {
            throw new ServletException(throwables);
        }

        req.getRequestDispatcher("/WEB-INF/views/user-profile.jsp").forward(req, resp);
    }
}
