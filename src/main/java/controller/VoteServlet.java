package controller;

import controller.util.InputValidator;
import model.comment.Comment;
import model.comment.CommentDAO;
import model.commentvote.CommentVote;
import model.commentvote.CommentVoteDAO;
import model.persistence.ConPool;
import model.post.Post;
import model.post.PostDAO;
import model.postvote.PostVote;
import model.postvote.PostVoteDAO;
import model.user.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/vote")
public class VoteServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String _id = req.getParameter("id");
        String vote = req.getParameter("vote");
        String type = req.getParameter("type");

        if (_id == null || !InputValidator.assertInt(_id) || type == null || vote == null
                || (!vote.equalsIgnoreCase("upvote") && !vote.equalsIgnoreCase("downvote"))
                || (!type.equalsIgnoreCase("post") && !type.equalsIgnoreCase("comment"))) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int id = Integer.parseInt(_id);

        if(type.equalsIgnoreCase("post"))
            try(Connection con = ConPool.getConnection()){
                PostVoteDAO pvd = new PostVoteDAO(con);
                PostDAO pd = new PostDAO(con);

                Post post = pd.get(id);
                if(post == null) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                User user = (User) req.getAttribute("loggedUser");
                int _vote = (vote.equalsIgnoreCase("upvote") ? 1 : -1);

                PostVote pv = new PostVote();
                pv.setPost(post);
                pv.setUser(user);
                pv.setVote((short) _vote);

                pvd.insert(pv);
            } catch (SQLException throwables) {
                throw new ServletException(throwables);
            }
        else
            try(Connection con = ConPool.getConnection()){
                CommentVoteDAO cvd = new CommentVoteDAO(con);
                CommentDAO cd = new CommentDAO(con);

                Comment comment = cd.get(id);
                if(comment == null) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                User user = (User) req.getAttribute("loggedUser");
                int _vote = (vote.equalsIgnoreCase("upvote") ? 1 : -1);

                CommentVote cv = new CommentVote();
                cv.setComment(comment);
                cv.setUser(user);
                cv.setVote((short) _vote);

                cvd.insert(cv);
            } catch (SQLException throwables) {
                throw new ServletException(throwables);
            }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
