package controller;

import comment.Comment;
import comment.CommentDAO;
import comment.CommentSpecification;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/all")
public class ShowAllPosts extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        CommentDAO service = null;
        Map<Integer, ArrayList<Comment>> list;
        try {
            service = new CommentDAO(ConPool.getConnection());
            list = service.fetchHierarchy(1,false, -1, -1);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}

