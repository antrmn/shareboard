package controller;

import persistence.ConPool;
import persistence.Specification;
import post.Post;
import post.PostDAO;
import post.PostSpecificationBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@WebServlet("/findPosts")
public class ShowAllPosts extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String loggedUserId = req.getParameter("loggedUser");
        String votedBy = req.getParameter("votedBy");
        String sectionFollowedBy = req.getParameter("sectionFollowedBy");
        String sectionId = req.getParameter("section");
        String author = req.getParameter("author");
        String authorName = req.getParameter("authorName");
        String titleContains = req.getParameter("titleContains");
        String contentContains = req.getParameter("contentContains");
        String olderThan = req.getParameter("olderThan");
        String newerThan = req.getParameter("newerThan");
        String type = req.getParameter("type");
        String id = req.getParameter("idPost");
        String orderBy = req.getParameter("orderBy");
        String descending = req.getParameter("descending");
        String limit = req.getParameter("limit");
        String offset = req.getParameter("offset");

        PostSpecificationBuilder psb = new PostSpecificationBuilder();


        if(loggedUserId != null && !loggedUserId.isBlank())
            psb.loggedUser(Integer.parseInt(loggedUserId));
        if(votedBy != null && !votedBy.isBlank())
            psb.isVotedBy(Integer.parseInt(votedBy));
        if(sectionFollowedBy != null && !sectionFollowedBy.isBlank())
            psb.isSectionFollowedByUser(Integer.parseInt(sectionFollowedBy));
        if(sectionId != null && !sectionId.isBlank())
            psb.isInSection(Integer.parseInt(sectionId));
        if(author != null && !author.isBlank())
            psb.isAuthor(Integer.parseInt(author));
        if(authorName != null && !authorName.isBlank())
            psb.isAuthor(authorName);
        if(titleContains != null && !titleContains.isBlank())
            psb.doesTitleContain(titleContains);
        if(contentContains != null && !contentContains.isBlank())
            psb.doesBodyContain(contentContains);
        if(type != null && !type.isBlank())
            psb.isType(Post.Type.valueOf(type));
        if(id != null && !id.isBlank())
            psb.byId(Integer.parseInt(id));
        if(olderThan != null && !olderThan.isBlank())
            psb.isOlderThan(LocalDate.parse(olderThan).atStartOfDay().toInstant(ZoneOffset.UTC));
        if(newerThan != null && !newerThan.isBlank())
            psb.isNewerThan(LocalDate.parse(newerThan).atStartOfDay().toInstant(ZoneOffset.UTC));
        if(limit != null && !limit.isBlank())
            psb.setLimit(Integer.parseInt(limit));
        if(offset != null && !offset.isBlank())
            psb.setOffset(Integer.parseInt(offset));
        if(orderBy != null && !orderBy.isBlank())
            if (orderBy.equalsIgnoreCase("voti"))
                psb.sortByVotes();
            else if (orderBy.equalsIgnoreCase("tempo"))
                psb.sortByTime();
        if(descending != null && !descending.isBlank())
            if(descending.equalsIgnoreCase("true"))
                psb.descendingOrder();
            else
                psb.ascendingOrder();

        Specification s = psb.build();
        try (Connection con = ConPool.getConnection()){
            PostDAO service = new PostDAO(con);
            List<Post> posts = service.fetch(s);

            req.setAttribute("posts", posts);
            req.getRequestDispatcher("/WEB-INF/views/test/show-all.jsp").forward(req, resp);
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}

