package controller;

import controller.util.ErrorForwarder;
import controller.util.FileUtils;
import model.persistence.ConPool;
import model.section.Section;
import model.section.SectionDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/newsection")
@MultipartConfig
public class NewSectionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/crm/create-section.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getAttribute("errors") != null) {
            doGet(req, resp);
            return;
        }
        //authenticate
        List<String> errors = new ArrayList<>();

        String name = req.getParameter("name");
        String description = req.getParameter("description");
        Part picture = req.getPart("picture");

        if(name == null || name.isBlank())
            errors.add("Specificare un titolo");

        Map<Integer, Section> sections = (Map<Integer, Section>) getServletContext().getAttribute("sections");

        for(Map.Entry<Integer, Section> entry : sections.entrySet()){
            if (entry.getValue().getName().equalsIgnoreCase(name))
                errors.add("Sezione già esistente");
        }

        if(picture!= null && picture.getSize() > 0){
            if (picture.getSize() > 5 * 1024 * 1024)
                errors.add("Il file non deve superare i 5MB");
            String mimeType = getServletContext().getMimeType(picture.getSubmittedFileName());
            if (mimeType == null || !mimeType.startsWith("image/"))
                errors.add("Il file caricato non è un'immagine");

        }
        if(!errors.isEmpty()){
            ErrorForwarder.sendError(req, resp, errors, 400, "/admin/newsection");
            return;
        }

        try(Connection con = ConPool.getConnection()){
            con.setAutoCommit(false);
            try(InputStream fileStream = picture.getInputStream()){
                SectionDAO service = new SectionDAO(con);
                Section s = new Section();
                s.setDescription(description);
                s.setName(name);
                if (picture!= null && picture.getSize() > 0){
                    s.setPicture(FileUtils.generateFileName(picture));
                    String uploadRoot = FileServlet.BASE_PATH;
                    File file = new File(uploadRoot + s.getPicture());
                    if(!file.getParentFile().exists())
                        file.getParentFile().mkdir();
                    Files.copy(fileStream, file.toPath());
                }
                service.insert(s);
                UpdateSectionsServlet.updateSections(getServletContext());
            } catch(SQLException | IOException e){
                con.rollback();
                throw new ServletException(e);
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e2) {
            throw new ServletException(e2);
        }

        resp.sendRedirect(getServletContext().getContextPath()+"/admin/showsections");
    }
}
