package controller;


import persistence.ConPool;
import persistence.Specification;
import post.Post;
import post.PostDAO;
import section.SectionDAO;
import section.SectionSpecificationBuilder;

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

@WebServlet("/s/*")
public class Section extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String path = (req.getPathInfo() != null) ? req.getPathInfo() : "/";
        System.out.println(path);

        switch (path){
            case"/":
                //redirect home
                resp.sendRedirect("./home");
               break;
            default:
                SectionSpecificationBuilder spb = new SectionSpecificationBuilder();
                spb.byName("test");
                Specification s = spb.build();
                try (Connection con = ConPool.getConnection()){
                    SectionDAO service = new SectionDAO(con);
                    List<section.Section> sections = service.fetch(s);
                    if(sections.isEmpty()){
                        //redirect home
                        resp.sendRedirect("./home");
                    } else{
                        section.Section section = sections.get(0);
                        System.out.println(section.getName());
                        req.setAttribute("section", section);
                        req.getRequestDispatcher("/WEB-INF/views/section.jsp").forward(req,resp);
                    }
                } catch(SQLException | NamingException e){
                    e.printStackTrace();
                    break;
                }
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // doGet(req,resp);
    }
}

