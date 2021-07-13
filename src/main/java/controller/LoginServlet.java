package controller;


import controller.util.ErrorForwarder;
import model.follow.FollowDAO;
import model.persistence.ConPool;
import model.user.User;
import model.user.UserDAO;

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
import java.util.Set;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getAttribute("loggedUser") != null){
            resp.sendRedirect(req.getContextPath());
            return;
        }
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getAttribute("loggedUser") != null){
            resp.sendRedirect(req.getContextPath());
            return;
        }
        if(req.getAttribute("errors") != null) {
            doGet(req, resp);
            return;
        }

        ArrayList<String> errors = new ArrayList<>();

        String username = req.getParameter("username");
        String password = req.getParameter("pass");

        username = (username == null ? null : username.trim());
        password = (password == null ? null : password.substring(0, Math.min(password.length(), 255))); //tronca password con >255 caratteri

        if(username == null || username.isEmpty())
            errors.add("Specificare il nome utente");
        if(password == null || password.isEmpty())
            errors.add("Specificare la password");

        if(!errors.isEmpty()) {
            ErrorForwarder.sendError(req, resp, errors, 400, "/login");
            return;
        }

        try (Connection con = ConPool.getConnection()) {
            UserDAO service = new UserDAO(con);
            User user = service.get(username, true);
            if (user != null && user.getPassword().check(password) ) {
                HttpSession session = req.getSession(true);
                session.setAttribute("loggedUserId", user.getId());

                /* Aggiunge nel db le sezioni seguite da guest */
                Set<Integer> follows = (Set<Integer>) session.getAttribute("userFollows");
                if(!follows.isEmpty()) {
                    FollowDAO followService = new FollowDAO(con);
                    followService.insert(follows, user.getId());
                }

                resp.sendRedirect(req.getContextPath());
            } else {
                ErrorForwarder.sendError(req, resp, List.of("Credenziali non valide"), 400,
                                                                                    "/login");
                return;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}