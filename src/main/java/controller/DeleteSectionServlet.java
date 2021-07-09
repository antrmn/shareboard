package controller;

import model.persistence.ConPool;
import model.section.SectionDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

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
                UpdateSectionsServlet.updateSections(getServletContext());
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
