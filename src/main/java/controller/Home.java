package controller;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/home")
public class Home extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //HomeDAO service = new HomeDAO();
        int id = 0;
//        try {
//            id = Integer.parseInt(req.getParameter("p"));
//        } catch(NullPointerException | NumberFormatException e){
//            throw new RuntimeException();
//        }

//        Post post = service.doRetrieve(id);
//        req.setAttribute("post", post);
        req.getRequestDispatcher("/WEB-INF/home.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}

