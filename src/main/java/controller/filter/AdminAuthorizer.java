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

@WebFilter(filterName = "adminAuthorizer")
public class AdminAuthorizer  extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpSession session = req.getSession(false);
        int id = (int)session.getAttribute("loggedUserId");
        User user = null;
        try(Connection con = ConPool.getConnection()){
            UserDAO service = new UserDAO(con);
            user = service.get(id, false);
            if (!user.getAdmin()){
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Non sei autorizzato.");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        chain.doFilter(req, res);
    }
}
