package filter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserAuthorizer  extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        if(req.getSession(false) == null || req.getSession(false).getAttribute("loggedUser") == null){
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Login richiesto.");
        }
        chain.doFilter(req, res);
    }
}
