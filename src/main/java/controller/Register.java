package controller;

import persistence.ConPool;
import user.HashedPassword;
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

@WebServlet("/register")
public class Register extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("mail");
        String username = req.getParameter("username");
        String pass = req.getParameter("pass");

        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(HashedPassword.hash(pass));

        Integer id = null;
        try(Connection con = ConPool.getConnection()){
            UserDAO service = new UserDAO(con);
            id = service.insert(user);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        if(id != null){
            req.getSession(true).setAttribute("loggedUserId", id);
        }
        resp.sendRedirect(getServletContext().getContextPath());
    }
}