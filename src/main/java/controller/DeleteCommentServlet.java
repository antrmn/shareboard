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

@WebServlet("/deletecomment")
public class DeleteCommentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String _commentId = req.getParameter("id");
        if(_commentId == null || _commentId.isBlank() || !InputValidator.assertInt(_commentId)){
            ErrorForwarder.sendError(req, resp, "Id non valido o non specificato", 400);
            return;
        }
        int commentId = Integer.parseInt(_commentId);

        Comment comment;
        User loggedUser = (User) req.getAttribute("loggedUser");
        try(Connection con = ConPool.getConnection()){
            CommentDAO service = new CommentDAO(con);
            comment = service.get(commentId);
            if(comment == null) {
                ErrorForwarder.sendError(req, resp, "Il commento non esiste", 400);
                return;
            }

            if(!loggedUser.getId().equals(comment.getAuthor().getId()) && loggedUser.getAdmin().equals(false)) {
                ErrorForwarder.sendError(req, resp, "Non sei autorizzato", HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            if(service.delete(comment.getId()) < 1)
                throw new RuntimeException("Post non eliminato");
        } catch (SQLException throwables) {
            throw new ServletException(throwables);
        }

        resp.sendRedirect(getServletContext().getContextPath() + "/post/" + comment.getPost().getId());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
