package controller;

import controller.util.InputValidator;
import model.follow.FollowDAO;
import model.persistence.ConPool;
import model.section.Section;
import model.user.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

@WebServlet("/unfollow")
public class UnfollowServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<Integer, Section> sections =
                (Map<Integer,Section>) getServletContext().getAttribute("sections");

        String section = req.getParameter("section");
        if (section == null || !InputValidator.assertInt(section)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int sectionId = Integer.parseInt(req.getParameter("section"));
        if (!sections.containsKey(sectionId)){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }

        HttpSession session = req.getSession(true);
        User user = (User) req.getAttribute("loggedUser");
        if(user == null){
            Set<Integer> follows = (Set<Integer>) session.getAttribute("userFollows");
            follows.remove(sectionId);
        } else {
            try(Connection con = ConPool.getConnection()){
                FollowDAO service = new FollowDAO(con);
                service.delete(user.getId(), sectionId);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
