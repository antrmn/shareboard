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

@WebServlet("/admin/toggleAdmin")
public class ToggleAdminServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer userId = Integer.parseInt(req.getParameter("userId"));
        Boolean isAdmin = Boolean.parseBoolean(req.getParameter("isAdmin")) ;
        if(userId == null || userId.equals(0)){
            ErrorForwarder.sendError(req, resp, "Specificare un utente", 400);
            return;
        }

        try(Connection con = ConPool.getConnection()){
            UserDAO service = new UserDAO(con);
            User user = service.get(userId, false);
            if(user == null){
                ErrorForwarder.sendError(req, resp, "L'utente specificato non esiste", 400);
                return;
            }


            if(isAdmin){
                service.setAdmin(user);
            } else{
                service.removeAdmin(user);
            }
        } catch (SQLException throwables) {
            throw new ServletException(throwables);
        }
    }
}
