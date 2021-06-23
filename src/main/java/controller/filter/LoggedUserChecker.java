package controller.filter;

import model.persistence.ConPool;
import model.user.User;
import model.user.UserDAO;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebFilter(filterName = "loggedUserChecker", asyncSupported = true)
public class LoggedUserChecker extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpSession session = req.getSession(false);
        if(session == null || session.getAttribute("loggedUserId") == null) {
            chain.doFilter(req, res);
            //System.out.println("sono un test e vado rimosso");
            return;
        }

        int id = (int)session.getAttribute("loggedUserId");
        User user = null;
        try(Connection con = ConPool.getConnection()){
            UserDAO service = new UserDAO(con);
            user = service.get(id, false);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(user == null){
            session.removeAttribute("loggedUserId");
        }
        req.setAttribute("loggedUser", user);
        chain.doFilter(req, res);
    }
}
