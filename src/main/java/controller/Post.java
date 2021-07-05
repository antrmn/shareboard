package controller;

import controller.util.ErrorForwarder;
import controller.util.InputValidator;
import model.comment.Comment;
import model.comment.CommentDAO;
import model.persistence.ConPool;
import model.post.PostDAO;
import model.post.PostSpecificationBuilder;
import model.user.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

@WebServlet("/post")
public class Post extends HttpServlet {
    @Override
    public void init() throws ServletException {
        getServletContext().setAttribute("maxCommentDepth", 3);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String _postId = req.getParameter("id");
        String _commentId = req.getParameter("comment");

        if(_postId == null || !InputValidator.assertInt(_postId)){
            ErrorForwarder.sendError(req, resp, "Specificare un post", 400);
            return;
        }

        User loggedUser = (User) req.getAttribute("loggedUser");
        int postId = Integer.parseInt(_postId);
        try(Connection con = ConPool.getConnection()){
            PostDAO service = new PostDAO(con);
            PostSpecificationBuilder psb = new PostSpecificationBuilder();
            model.post.Post post = null;
            if(loggedUser != null){
                post = service.get(postId, loggedUser.getId());
            } else {
                post = service.get(postId);
            }
            if(post == null) {
                ErrorForwarder.sendError(req, resp, "Il post specificato non esiste", 400);
                return;
            } else {
                req.setAttribute("post", post);
            }
        } catch (SQLException throwables) {
            throw new ServletException(throwables);
        }


        try(Connection con = ConPool.getConnection()){
            CommentDAO service = new CommentDAO(con);

            Comment comment = null;
            if(_commentId != null && InputValidator.assertInt(_commentId)) {
                int commentId = Integer.parseInt(_commentId);
                comment = service.get(commentId);
                if(comment != null && comment.getPost().getId() != postId)
                    comment = null;
            }

            Map<Integer, ArrayList<Comment>> comments;
            if(comment != null) {
                comments = service.fetchHierarchy(comment.getId(), true,
                        (Integer) getServletContext().getAttribute("maxCommentDepth"),
                        loggedUser != null ? loggedUser.getId() : -1);
                req.setAttribute("initialIndex", comment.getParentComment().getId());
            } else {
                comments = service.fetchHierarchy(postId, false,
                        (Integer) getServletContext().getAttribute("maxCommentDepth"),
                        loggedUser != null ? loggedUser.getId() : -1);
                req.setAttribute("initialIndex", 0);
            }

            req.setAttribute("comments", comments);
        } catch (SQLException throwables) {
            throw new ServletException(throwables);
        }

        req.getRequestDispatcher("/WEB-INF/views/section/post.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
}
