package controller;

import controller.util.ErrorForwarder;
import controller.util.InputValidator;
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

@WebServlet("/deletepost")
public class DeletePostServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String _postId = req.getParameter("id");
        if(_postId == null || _postId.isBlank() || !InputValidator.assertInt(_postId)){
            ErrorForwarder.sendError(req, resp, "Id non valido o non specificato", 400);
            return;
        }
        int postId = Integer.parseInt(_postId);

        Post post;
        User loggedUser = (User) req.getAttribute("loggedUser");
        try(Connection con = ConPool.getConnection()){
            PostDAO service = new PostDAO(con);
            post = service.get(postId);
            if(post == null) {
                ErrorForwarder.sendError(req, resp, "Il post non esiste", 400);
                return;
            }

            if(!loggedUser.getId().equals(post.getAuthor().getId()) && loggedUser.getAdmin().equals(false)) {
                ErrorForwarder.sendError(req, resp, "Non sei autorizzato", HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            if(service.delete(post.getId()) < 1)
                throw new RuntimeException("Post non eliminato");
        } catch (SQLException throwables) {
            throw new ServletException(throwables);
        }

        resp.sendRedirect(getServletContext().getContextPath() + "/s?section=" + post.getSection().getName());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
