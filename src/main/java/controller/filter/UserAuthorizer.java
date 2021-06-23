package controller.filter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "userAuthorizer")
public class UserAuthorizer  extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        if(req.getAttribute("loggedUser") == null){
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Login richiesto.");
            return;
        }
        chain.doFilter(req, res);
    }
}
