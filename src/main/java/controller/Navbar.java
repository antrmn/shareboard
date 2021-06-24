package controller;


import model.follow.Follow;
import model.follow.FollowDAO;
import model.follow.FollowSpecificationBuilder;
import model.persistence.ConPool;
import model.persistence.Specification;
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
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@WebServlet("/navbar")
public class Navbar extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ConcurrentMap<Integer, model.section.Section> sections =
                (ConcurrentMap<Integer, Section>) getServletContext().getAttribute("sections");


        HttpSession session = req.getSession(true);
        User user = (User) req.getAttribute("loggedUser");
        if(user == null){
            Set<Integer> follows = (Set<Integer>) session.getAttribute("follows");
            req.setAttribute("follows", follows);
            req.getRequestDispatcher("/WEB-INF/views/partials/navbar.jsp").include(req,resp);
        } else {
            try(Connection con = ConPool.getConnection()){
                FollowDAO service = new FollowDAO(con);
                FollowSpecificationBuilder fsb = new FollowSpecificationBuilder();
                fsb.byUserId(user.getId());
                Specification s = fsb.build();
                List<Follow> follows = service.fetch(s);
                Set<Integer> test = ConcurrentHashMap.newKeySet();
                for(Follow follow : follows){
                   test.add(follow.getSection().getId());
                }
                req.setAttribute("follows", test);
                req.getRequestDispatcher("/WEB-INF/views/partials/navbar.jsp").include(req,resp);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {}
}