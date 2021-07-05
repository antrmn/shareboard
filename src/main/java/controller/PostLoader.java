package controller;

import controller.util.InputValidator;
import controller.util.Paginator;
import model.persistence.ConPool;
import model.post.Post;
import model.post.PostDAO;
import model.post.PostSpecificationBuilder;
import model.user.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;

@WebServlet("/loadposts")
public class PostLoader extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String content = req.getParameter("content");
        String onlyFollow = req.getParameter("onlyfollow");
        String section = req.getParameter("section");
        String author = req.getParameter("author");
        String orderBy = req.getParameter("orderby");
        String _postedAfter = req.getParameter("postedafter");
        String _postedBefore = req.getParameter("postedBefore");
        String _page = req.getParameter("page");

        int page;
        if (_page != null && !_page.isBlank() && InputValidator.assertInt(_page))
            page = Integer.parseInt(_page);
        else
            page = 1;

        User loggedUser = (User) req.getAttribute("loggedUser");

        PostSpecificationBuilder psb = new PostSpecificationBuilder();
        if(content != null && !content.isBlank()) {
            psb.doesTitleOrBodyContains(content);
        }
        if(onlyFollow != null && onlyFollow.equals("on")){
            if(loggedUser == null){
                Set<Integer> follows = (Set<Integer>) req.getSession(true).getAttribute("follows");
                if(follows.isEmpty())
                    psb.isInSection(-1);
                else
                    psb.isInSection(follows);
            } else {
                psb.isSectionFollowedByUser(loggedUser.getId());
            }
        }
        if(section != null && !section.isBlank()){
            psb.isInSectionByName(section);
        }
        if(author != null && !author.isBlank()){
            psb.isAuthor(author);
        }
        if(_postedAfter != null && !_postedAfter.isBlank()){
            Instant postedAfter = LocalDate.parse(_postedAfter).atStartOfDay().toInstant(ZoneOffset.UTC);
            psb.isNewerThan(postedAfter);
        }
        if(_postedBefore != null && !_postedBefore.isBlank()){
            Instant postedBefore = LocalDate.parse(_postedBefore).atStartOfDay().toInstant(ZoneOffset.UTC);
            psb.isOlderThan(postedBefore);
        }
        if(orderBy != null && !orderBy.isBlank()){
            if(orderBy.equalsIgnoreCase("newest"))
                psb.sortByTime().descendingOrder();
            else if (orderBy.equalsIgnoreCase("oldest"))
                psb.sortByTime().ascendingOrder();
            else if (orderBy.equalsIgnoreCase("mostvoted"))
                psb.sortByVotes().descendingOrder();
            else if (orderBy.equalsIgnoreCase("leastvoted"))
                psb.sortByVotes().ascendingOrder();
        }

        Paginator p = new Paginator(page, 10);
        psb.setLimit(p.getLimit());
        psb.setOffset(p.getOffset());

        try(Connection con = ConPool.getConnection()){
            PostDAO service = new PostDAO(con);
            List<Post> posts = service.fetch(psb.build());
            req.setAttribute("posts",posts);
            req.getRequestDispatcher("/WEB-INF/views/partials/post-previews.jsp").forward(req,resp);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
