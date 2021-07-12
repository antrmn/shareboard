package controller;

import controller.util.ErrorForwarder;
import model.ban.Ban;
import model.comment.Comment;
import model.comment.CommentDAO;
import model.persistence.ConPool;
import model.post.Post;
import model.post.PostDAO;
import model.user.User;

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

@WebServlet("/newcomment")
public class NewCommentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //todo: input check.

        int postId = Integer.parseInt(req.getParameter("id"));
        int parentId = Integer.parseInt(req.getParameter("parent"));
        String text = req.getParameter("text");

        try (Connection con = ConPool.getConnection()){
            PostDAO servicePosts = new PostDAO(con);
            Post post = servicePosts.get(postId);
            List<Ban> bans = (List<Ban>) req.getAttribute("loggedUserBans");
            if(bans.stream().anyMatch(ban -> ban.getSection().getId().equals(post.getSection().getId())
                                             || ban.getGlobal().equals(true))){
                ErrorForwarder.sendError(req, resp, "Ti è stato impedito di commentare in questa sezione", 403);
                return;
            }

            Comment c = new Comment();
            Comment parent = new Comment();
            if (parentId > 0){
                parent.setId(parentId);
            }
            User u = new User();
            HttpSession session = req.getSession(true);
            u.setId((Integer)session.getAttribute("loggedUserId"));
            c.setAuthor(u);
            c.setText(text);
            c.setParentComment(parent);
            Post parentPost = new Post();
            parentPost.setId(postId);
            c.setPost(parentPost);
            List<Comment> comments = new ArrayList<>();
            comments.add(c);
            CommentDAO service = new CommentDAO(con);
            service.insert(comments);

        } catch(SQLException   e){
            throw new ServletException(e);
        }
        resp.sendRedirect(req.getContextPath() + "/post/" + postId);
    }
}
