package controller;


import model.persistence.ConPool;
import model.user.User;
import model.user.UserDAO;
import model.user.UserSpecificationBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/showusers")
public class ShowUsersServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection con = ConPool.getConnection()){
            UserDAO service = new UserDAO(con);
            UserSpecificationBuilder usb = new UserSpecificationBuilder();
            List<User> users = service.fetch(usb.build());
            req.setAttribute("users", users);
            req.getRequestDispatcher("/WEB-INF/views/crm/show-users.jsp").forward(req,resp);
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
}
