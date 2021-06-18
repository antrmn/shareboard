package controller;


import persistence.ConPool;
import user.User;
import user.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/login")
public class Login extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("pass");

        try (Connection con = ConPool.getConnection()) {
            UserDAO service = new UserDAO(con);
            User user = service.get(username, true);
            if (user != null) {
                boolean success = user.getPassword().check(password);
                System.out.println(success);
                if(success){
                    req.getSession(true).setAttribute("loggedUserId", user.getId());
                    resp.sendRedirect(req.getContextPath());
                }
            } else {
                // TODO: errore "non esiste"
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}