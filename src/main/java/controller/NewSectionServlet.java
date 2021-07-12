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
import java.nio.file.Path;
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
        Part banner = req.getPart("banner");

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
        if(banner!= null && banner.getSize() > 0){
            if (banner.getSize() > 5 * 1024 * 1024)
                errors.add("Il file per il banner non deve superare i 5MB");
            String mimeType = getServletContext().getMimeType(picture.getSubmittedFileName());
            if (mimeType == null || !mimeType.startsWith("image/"))
                errors.add("Il file caricato per il banner non è un'immagine");
        }
        if(!errors.isEmpty()){
            ErrorForwarder.sendError(req, resp, errors, 400, "/admin/newsection");
            return;
        }

        String newPicture = null;
        String newBanner = null;
        try(Connection con = ConPool.getConnection()) {
            SectionDAO service = new SectionDAO(con);
            Section s = new Section();
            s.setDescription(description);
            s.setName(name);

            if (picture != null && picture.getSize() > 0)
                try (InputStream fileStream = picture.getInputStream()) {
                    newPicture = FileUtils.generateFileName(picture);
                    s.setPicture(FileUtils.generateFileName(picture));
                    String uploadRoot = FileServlet.BASE_PATH;
                    File file = new File(uploadRoot + s.getPicture());
                    Files.createDirectories(file.toPath().getParent());
                    Files.copy(fileStream, file.toPath());
                }

            if (banner != null && banner.getSize() > 0)
                try (InputStream fileStream = banner.getInputStream()) {
                    newBanner = FileUtils.generateFileName(banner);
                    s.setBanner(FileUtils.generateFileName(picture));
                    String uploadRoot = FileServlet.BASE_PATH;
                    File file = new File(uploadRoot + s.getBanner());
                    Files.createDirectories(file.toPath().getParent());
                    Files.copy(fileStream, file.toPath());
                }

            service.insert(s);
        } catch (SQLException | IOException e) {
            Files.delete(Path.of(FileServlet.BASE_PATH + newPicture));
            Files.delete(Path.of(FileServlet.BASE_PATH + newBanner));
            throw new ServletException(e);
        }

        try {
            UpdateSectionsServlet.updateSections(req.getServletContext());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        resp.sendRedirect(getServletContext().getContextPath()+"/admin/showsections");
    }
}
