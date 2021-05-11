package controller;

import model.ConPool;
import post.Post;
import post.PostDAO;
import post.PostSpecification;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/post")
public class ShowPost extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PostDAO service = null;
        try {
            service = new PostDAO(ConPool.getConnection());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }
        int id = 0;
        try {
            id = Integer.parseInt(req.getParameter("p"));
        } catch(NullPointerException | NumberFormatException e){
            throw new RuntimeException();
        }

        List<Post> posts = null;
        try {
            posts = service.fetch(new PostSpecification().setLimit(50));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        req.setAttribute("post", posts);
        req.getRequestDispatcher("/WEB-INF/show-post.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}

