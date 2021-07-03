package controller;

import controller.util.ErrorForwarder;
import controller.util.InputValidator;
import model.persistence.ConPool;
import model.post.Post;
import model.post.PostDAO;
import model.section.Section;
import model.section.SectionDAO;
import model.section.SectionSpecificationBuilder;
import model.user.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/admin/deletesection")
public class DeleteSectionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int sectionId = Integer.parseInt(req.getParameter("sectionId"));
        try(Connection con = ConPool.getConnection()){
            SectionDAO service = new SectionDAO(con);

            if(service.delete(sectionId) < 1)
                throw new RuntimeException("Sezione non eliminata");
            else{
                List<Section> _sections = service.fetch(new SectionSpecificationBuilder().sortById().build());
                getServletContext().setAttribute("sections", _sections.stream().collect(Collectors.toConcurrentMap(x -> x.getId(), x -> x)));
            }
        } catch (SQLException throwables) {
            throw new ServletException(throwables);
        }

        resp.sendRedirect(getServletContext().getContextPath() + "/admin/showsections");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
