package controller;

import comment.Comment;
import comment.CommentDAO;
import persistence.ConPool;
import user.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/new-comment")
public class CommentAdder extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int postId = Integer.parseInt(req.getParameter("id"));
        int parentId = Integer.parseInt(req.getParameter("parent"));
        String text = req.getParameter("text");


        System.out.println(postId);
        System.out.println(parentId);
        System.out.println(text);
        try (Connection con = ConPool.getConnection()){
            Comment c = new Comment();
            Comment parent = new Comment();
            if (parentId == 0){
                parent.setId(parentId);
            }
            User u = new User();
            HttpSession session = req.getSession(true);
            u.setId((Integer)session.getAttribute("loggedUserId"));
            c.setAuthor(u);
            c.setText(text);
            c.setParentComment(parent);
            post.Post parentPost = new post.Post();
            parentPost.setId(postId);
            c.setPost(parentPost);
            List<Comment> comments = new ArrayList<>();
            CommentDAO service = new CommentDAO(con);
            service.insert(comments);
            resp.sendRedirect("./post?id=" + postId);
        } catch(SQLException   e){
            e.printStackTrace();
        }
    }
}
