package controller;

import persistence.ConPool;
import post.Post;
import post.PostDAO;
import post.PostSpecificationBuilder;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/findPosts")
public class ShowAllPosts extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String title = req.getParameter("title");

        try {
            Connection con = ConPool.getConnection();
            PostDAO service = new PostDAO(con);
            PostSpecificationBuilder builder = new PostSpecificationBuilder();
            if (title != null)
                builder.doesTitleOrBodyContains(title);
            List<Post> posts = service.fetch(builder.build());

            req.setAttribute("posts", posts);
            req.getRequestDispatcher("/WEB-INF/show-all.jsp").forward(req, resp);
        } catch(SQLException | NamingException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}

