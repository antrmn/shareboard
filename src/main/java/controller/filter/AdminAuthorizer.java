package controller.filter;

import model.user.User;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "adminAuthorizer")
public class AdminAuthorizer  extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        User loggedUser = (User) req.getAttribute("loggedUser");
        if(loggedUser == null){
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Effettuare il login");
            return;
        }
        if(loggedUser.getAdmin().equals(false)){
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Non hai i permessi necessari");
            return;
        }
        chain.doFilter(req, res);
    }
}
