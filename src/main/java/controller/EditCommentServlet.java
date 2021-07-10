package controller;

import controller.util.ErrorForwarder;
import controller.util.InputValidator;
import model.comment.Comment;
import model.comment.CommentDAO;
import model.persistence.ConPool;
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
import java.util.List;

@WebServlet("/editcomment")
public class EditCommentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> errors = new ArrayList<>();

        String _commentId = req.getParameter("id");
        String text = req.getParameter("text");

        if(_commentId == null || _commentId.isBlank() || !InputValidator.assertInt(_commentId)) {
            errors.add("Specificare un commento");
        }
        if(text == null || text.isBlank()){
            errors.add("Specificare testo");
        }
        if(!errors.isEmpty()){
            ErrorForwarder.sendError(req, resp, errors, 400);
            return;
        }

        int commentId = Integer.parseInt(_commentId);
        int parentPostId;
        try(Connection con = ConPool.getConnection()){
            CommentDAO commentService = new CommentDAO(con);
            Comment comment = commentService.get(commentId);
            if (comment == null){
                ErrorForwarder.sendError(req, resp, List.of("Il commento specificato non esiste"), 400);
                return;
            }
            User loggedUser = (User) req.getAttribute("loggedUser");
            if(!loggedUser.getId().equals(comment.getAuthor().getId()) && loggedUser.getAdmin().equals(false)){
                ErrorForwarder.sendError(req, resp, List.of(""), 400);
                return;
            }

            comment.setText(text);
            commentService.update(comment);
            parentPostId = comment.getPost().getId();
        } catch (SQLException throwables) {
            throw new ServletException(throwables);
        }

        resp.sendRedirect( getServletContext().getContextPath() + "/post/" + parentPostId);
    }
}
