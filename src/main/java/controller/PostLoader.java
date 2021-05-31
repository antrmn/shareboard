package controller;

import com.google.gson.Gson;
import persistence.ConPool;
import persistence.Specification;
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
import java.util.Enumeration;
import java.util.List;

@WebServlet("/loadPosts")
public class PostLoader extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { System.out.print("TEST");
        System.out.print( req.getParameterNames());

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PostSpecificationBuilder psb = new PostSpecificationBuilder();
        psb.isInSectionByName(req.getParameter("section"));
        Specification s = psb.build();
        try (Connection con = ConPool.getConnection()){
            PostDAO service = new PostDAO(con);
            List<Post> posts = service.fetch(s);
            Gson gson = new Gson();
            System.out.print(req.getParameter("section"));
            System.out.print(gson.toJson(posts));
            resp.getWriter().write(gson.toJson(posts));
//            req.setAttribute("posts", posts);
//            req.getRequestDispatcher("/WEB-INF/show-all.jsp").forward(req, resp);
        } catch(SQLException | NamingException  e){
            e.printStackTrace();
        }
    }
}
