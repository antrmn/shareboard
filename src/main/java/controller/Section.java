package controller;

import model.persistence.ConPool;
import model.persistence.Specification;
import model.post.Post;
import model.post.PostDAO;
import model.post.PostSpecificationBuilder;
import model.section.SectionDAO;
import model.section.SectionSpecificationBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/s")
public class Section extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //String path = (req.getPathInfo() != null) ? req.getPathInfo() : "/";
        //System.out.println(path);
        String sectionName = req.getParameter("section");
        SectionSpecificationBuilder spb = new SectionSpecificationBuilder();
        spb.byName(sectionName);
        Specification s = spb.build();
        try (Connection con = ConPool.getConnection()){
            SectionDAO service = new SectionDAO(con);
            List<model.section.Section> sections = service.fetch(s);
            if(sections.isEmpty()){
                //redirect home
                resp.sendRedirect("./home");
            } else{
                model.section.Section section = sections.get(0);
                req.setAttribute("section", section);
                req.getRequestDispatcher("/WEB-INF/views/section/section.jsp").forward(req,resp);
            }
        } catch(SQLException  e){
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
}

