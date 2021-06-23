package controller;

import model.follow.FollowDAO;
import model.persistence.ConPool;
import model.user.HashedPassword;
import model.user.User;
import model.user.UserDAO;
import model.user.UserSpecificationBuilder;

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

@WebServlet("/register")
public class Register extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getAttribute("errors") != null) {
            doGet(req, resp);
            return;
        }

        List<String> errors = new ArrayList<>();

        String email = req.getParameter("mail");
        String username = req.getParameter("username");
        String pass = req.getParameter("pass");
        String pass2 = req.getParameter("pass2");

        email = (email == null ? null : email.trim());
        username = (username == null ? null : username.trim());

        if(username == null || username.isEmpty())
            errors.add("Specificare il nome utente");
        else if (!InputValidator.assertMatch(username, InputValidator.USERNAME_PATTERN))
            errors.add("Username non valido");
        else if (!(username.length() >= 3 && username.length() <= 30))
            errors.add("L'username deve essere di lunghezza compresa tra i 3 e i 30 caratteri");


        if(email == null || email.isEmpty())
            errors.add("Specificare l'email");
        else if (!InputValidator.assertEmail(email) || email.length() > 255)
            errors.add("Email non valida");

        if(pass == null || pass.isEmpty())
            errors.add("Specificare la password");
        else if (!(pass.length()>=3 && pass.length()<=255))
            errors.add("La password deve essere di lunghezza compresa tra i 3 e i 255 caratteri");
        else if (pass2 == null || pass2.isEmpty())
            errors.add("Occorre confermare la password");
        else if (!pass.equals(pass2))
            errors.add("Le password non coincidono");

        if(!errors.isEmpty()){
            ErrorForwarder.sendError(req, resp, errors, 400, "/register");
            return;
        }

        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(HashedPassword.hash(pass));

        Integer id = null;
        try(Connection con = ConPool.getConnection()){
            UserDAO service = new UserDAO(con);

            if(service.get(username, false) != null)
                errors.add("Username già usato");
            if(!service.fetch(new UserSpecificationBuilder().byEmail(email).build())
                       .isEmpty())
                errors.add("L'email appartiene già a un altro utente");

            if(!errors.isEmpty()){
                ErrorForwarder.sendError(req, resp, errors, 400, "/register");
                return;
            }

            id = service.insert(user);
            HttpSession session = req.getSession(true);
            session.setAttribute("loggedUserId", id);

            Set<Integer> follows = (Set<Integer>) session.getAttribute("follows");
            if(!follows.isEmpty()) {
                FollowDAO followService = new FollowDAO(con);
                followService.insert(follows, user.getId());
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        resp.sendRedirect(getServletContext().getContextPath());
    }
}