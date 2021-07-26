package controller;

import controller.util.ErrorForwarder;
import controller.util.InputValidator;
import model.ban.Ban;
import model.comment.Comment;
import model.comment.CommentDAO;
import model.persistence.ConPool;
import model.post.Post;
import model.post.PostDAO;
import model.user.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Function;

@WebServlet("/newcomment")
public class NewCommentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String _postId = req.getParameter("id");
        String _parentId = req.getParameter("parent");
        String text = req.getParameter("text");

        if(text == null || text.isBlank()){
            ErrorForwarder.sendError(req, resp, "Il commento non può essere vuoto", 400);
            return;
        }

        int postId;
        List<Ban> bans = (List<Ban>) req.getAttribute("loggedUserBans");
        Function<Integer, Boolean> hasBan = id -> bans.stream().anyMatch(ban -> ban.getSection().getId().equals(id)
                                                                                    || ban.getGlobal().equals(true));
        if((_postId != null && InputValidator.assertInt(_postId))){
            postId = Integer.parseInt(_postId);
            try (Connection con = ConPool.getConnection()) {
                PostDAO service = new PostDAO(con);
                Post post = service.get(postId);
                if(post == null){
                    ErrorForwarder.sendError(req, resp, "Il post specificato non esiste", 400);
                    return;
                }
                if(!hasBan.apply(post.getSection().getId())){
                    ErrorForwarder.sendError(req, resp, "Non ti è permesso commentare in questa sezione", 403);
                    return;
                }
                CommentDAO service2 = new CommentDAO(con);
                Comment comment = new Comment();
                comment.setText(text);
                comment.setPost(post);
                comment.setAuthor((User) req.getAttribute("loggedUser"));
                service2.insert(comment);
            } catch (SQLException throwables) {
                throw new ServletException(throwables);
            }
        } else if (_parentId != null && InputValidator.assertInt(_parentId)){
            int parentId = Integer.parseInt(_parentId);
            try(Connection con = ConPool.getConnection()){
                CommentDAO service = new CommentDAO(con);
                Comment parentComment = service.get(parentId);
                if(parentComment == null){
                    ErrorForwarder.sendError(req, resp, "Il commento specificato non esiste", 400);
                    return;
                }
                int sectionId = new PostDAO(con).get(parentComment.getPost().getId()).getSection().getId();
                if(!hasBan.apply(sectionId)){
                    ErrorForwarder.sendError(req, resp, "Non ti è permesso commentare in questa sezione", 403);
                    return;
                }
                Comment comment = new Comment();
                comment.setText(text);
                comment.setAuthor((User) req.getAttribute("loggedUser"));
                comment.setParentComment(parentComment);
                comment.setPost(parentComment.getPost());
                service.insert(comment);
                postId = parentComment.getPost().getId();
            } catch (SQLException throwables) {
                throw new ServletException(throwables);
            }
        } else {
            ErrorForwarder.sendError(req, resp, "Specificare un commento o un post", 400);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/post/" + postId +"#comment-container");
    }
}
