package controller;


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
public class Login extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getAttribute("exception") != null) {
            doGet(req, resp);
            return;
        }

        ArrayList<String> errors = new ArrayList<>();

        String username = req.getParameter("username");
        String password = req.getParameter("pass");

        if(username == null || username.isBlank())
            errors.add("Specificare il nome utente");
        if(password == null || password.isBlank())
            errors.add("Specificare la password");

        if(!errors.isEmpty())
            throw new BadRequestException("Login fallito", errors, "/login");

        try (Connection con = ConPool.getConnection()) {
            UserDAO service = new UserDAO(con);
            User user = service.get(username, true);
            if (user != null && user.getPassword().check(password) ) {
                HttpSession session = req.getSession(true);
                session.setAttribute("loggedUserId", user.getId());

                /* Aggiunge nel db le sezioni seguite da guest */
                Set<Integer> follows = (Set<Integer>) session.getAttribute("follows");
                if(!follows.isEmpty()) {
                    FollowDAO followService = new FollowDAO(con);
                    followService.insert(follows, user.getId());
                }

                resp.sendRedirect(req.getContextPath());
            } else {
                throw new BadRequestException("Login fallito", List.of("Credenziali non valide"), "/login");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}