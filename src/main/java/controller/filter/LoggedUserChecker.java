package controller.filter;

import model.ban.Ban;
import model.ban.BanDAO;
import model.ban.BanSpecificationBuilder;
import model.follow.Follow;
import model.follow.FollowDAO;
import model.follow.FollowSpecificationBuilder;
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
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@WebFilter(filterName = "loggedUserChecker")
public class LoggedUserChecker extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpSession session = req.getSession(false);
        if(session == null || session.getAttribute("loggedUserId") == null) {
            chain.doFilter(req, res);
            return;
        }
        int id = (int)session.getAttribute("loggedUserId");

        User user = null;
        try(Connection con = ConPool.getConnection()){
            UserDAO service = new UserDAO(con);
            user = service.get(id, false);
            req.setAttribute("loggedUser", user);
        } catch (SQLException throwables) {
            throw new ServletException(throwables);
        }

        if(user != null){
            try(Connection con = ConPool.getConnection()){
                /* Carico i ban utente _attuali_ nella request */
                BanDAO service = new BanDAO(con);
                BanSpecificationBuilder bsb = new BanSpecificationBuilder().byUserId(id).endAfter(Instant.now());
                List<Ban> bans = service.fetch(bsb.build());
                req.setAttribute("loggedUserBans", bans);

                /* Carico gli id (solo gli id!) delle sezioni seguite */
                FollowDAO service2 = new FollowDAO(con);
                FollowSpecificationBuilder fsb = new FollowSpecificationBuilder().byUserId(id);
                List<Follow> _follows = service2.fetch(fsb.build());
                Set<Integer> follows = _follows.stream()
                                                .map(x -> x.getSection().getId())
                                                .collect(Collectors.toSet());
                req.setAttribute("userFollows", follows);
            } catch (SQLException throwables) {
                throw new ServletException(throwables);
            }
        }

        chain.doFilter(req, res);
    }
}
