package controller;

import controller.util.ErrorForwarder;
import controller.util.InputValidator;
import model.ban.Ban;
import model.ban.BanDAO;
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

@WebServlet("/admin/deleteban")
public class DeleteBanServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String _banId = req.getParameter("banId");
        if(_banId == null || _banId.isBlank() || !InputValidator.assertInt(_banId)){
            ErrorForwarder.sendError(req, resp, "Id non valido o non specificato", 400);
            return;
        }
        int banId = Integer.parseInt(_banId);

        try(Connection con = ConPool.getConnection()){
            BanDAO service = new BanDAO(con);
            Ban ban = service.get(banId);
            if(ban == null) {
                ErrorForwarder.sendError(req, resp, "Il ban non esiste", 400);
                return;
            }

            if(service.delete(ban.getId()) < 1)
                throw new RuntimeException("Ban non eliminato");
        } catch (SQLException throwables) {
            throw new ServletException(throwables);
        }

        resp.sendRedirect(getServletContext().getContextPath() + "/admin/showbans?userId=" + req.getParameter("userId"));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
