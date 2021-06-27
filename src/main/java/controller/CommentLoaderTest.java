package controller;

import model.comment.Comment;
import model.comment.CommentDAO;
import model.persistence.ConPool;
import model.user.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

@WebServlet("/loadCommentsTest")
public class CommentLoaderTest extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int postId = Integer.parseInt(req.getParameter("postId"));
        int userId = req.getAttribute("loggedUser") == null ?
                -1 : ((User)req.getAttribute("loggedUser")).getId();

        Map<Integer, ArrayList<Comment>> comments;
        try (Connection con = ConPool.getConnection()){
            CommentDAO service = new CommentDAO(con);
            comments = service.fetchHierarchy(5, true, 10, userId);
        } catch(SQLException e){
            throw new ServletException(e);
        }
        req.setAttribute("comments", comments);
        req.getRequestDispatcher("/WEB-INF/views/test/myTestJSP.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
