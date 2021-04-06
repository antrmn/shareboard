package controller;

import model.Post;
import model.PostDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/post")
public class ShowPost extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PostDAO service = new PostDAO();
        int id = 0;
        try {
            id = Integer.parseInt(req.getParameter("p"));
        } catch(NullPointerException | NumberFormatException e){
            throw new RuntimeException();
        }

        Post post = service.doRetrieve(id);
        req.setAttribute("post", post);
        req.getRequestDispatcher("/WEB-INF/show-post.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}

