package controller;

import com.google.gson.Gson;
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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/loadPosts")
public class PostLoader extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //se in home carica tutto
        //se in sezione carica in base ad essa
        //order by
        //limit e offset
        System.out.println(req.getParameter("section"));
        String section = req.getParameter("section");
//        String order = req.getParameter("order");
//        String offset = req.getParameter("offset");
        PostSpecificationBuilder psb = new PostSpecificationBuilder();
        if(!section.equalsIgnoreCase("Home")){
            psb.isInSectionByName(section);
        }
        Specification s = psb.build();
        try (Connection con = ConPool.getConnection()){
            PostDAO service = new PostDAO(con);
            List<Post> posts = service.fetch(s);
            Gson gson = new Gson();
//            System.out.println(gson.toJson(posts));
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            ArrayList<String> test = new ArrayList<>();
            test.add(gson.toJson(posts));
            resp.getWriter().print(gson.toJson(test));
            resp.getWriter().flush();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
}
