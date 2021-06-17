package controller;

import com.google.gson.Gson;
import comment.Comment;
import comment.CommentDAO;
import persistence.ConPool;

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

@WebServlet("/loadComments")
public class CommentLoader extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int postId = Integer.parseInt(req.getParameter("postId"));
        try (Connection con = ConPool.getConnection()){
            CommentDAO service = new CommentDAO(con);
            Map<Integer, ArrayList<Comment>> comments = service.fetchHierarchy(postId, false, 10, 0);
            Gson gson = new Gson();
//            System.out.println(req.getParameter("section"));
            //System.out.println(gson.toJson(comments));
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            ArrayList<String> test = new ArrayList<>();
            test.add(gson.toJson(comments));
            resp.getWriter().print(gson.toJson(test));
            resp.getWriter().flush();
        } catch(SQLException   e){
            e.printStackTrace();
        }
    }
}
