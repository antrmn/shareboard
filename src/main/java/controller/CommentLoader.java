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
import java.util.List;

@WebServlet("/loadComments")
public class CommentLoader extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //se in home carica tutto
        //se in sezione carica in base ad essa
        //order by
        //limit e offset
        String path = (req.getPathInfo() != null) ? req.getPathInfo() : "/";
        System.out.println(path);
        System.out.println(req.getParameter("section"));
        System.out.println("loadPosts");
        PostSpecificationBuilder psb = new PostSpecificationBuilder();
        psb.isInSectionByName("Desc");
        Specification s = psb.build();
        try (Connection con = ConPool.getConnection()){
            PostDAO service = new PostDAO(con);
            List<Post> posts = service.fetch(s);
            Gson gson = new Gson();
//            System.out.println(req.getParameter("section"));
//            System.out.println(gson.toJson(posts));
            resp.getWriter().write(gson.toJson(posts));
        } catch(SQLException | NamingException  e){
            e.printStackTrace();
        }
    }
}
