package controller;

import model.persistence.ConPool;
import model.persistence.Specification;
import model.post.Post;
import model.post.PostDAO;
import model.post.PostSpecificationBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/home")
public class Home extends HttpServlet {
    private static Logger logger = LogManager.getLogger(Home.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PostSpecificationBuilder psb = new PostSpecificationBuilder();
        HttpSession session = req.getSession(true);
        if ((session != null && session.getAttribute("loggedUserId") != null))
            psb.loggedUser((Integer) session.getAttribute("loggedUserId"));

        Specification s = psb.build();
        try (Connection con = ConPool.getConnection()){
            PostDAO service = new PostDAO(con);
            List<Post> posts = service.fetch(s);
            req.setAttribute("posts", posts);
        } catch(SQLException e){
            e.printStackTrace();
        }
        req.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       doGet(req,resp);
    }
}

