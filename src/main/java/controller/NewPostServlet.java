package controller;

import controller.util.ErrorForwarder;
import controller.util.FileUtils;
import controller.util.InputValidator;
import model.persistence.ConPool;
import model.post.Post;
import model.post.PostDAO;
import model.section.Section;
import model.user.User;

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

@WebServlet("/newpost")
@MultipartConfig
public class NewPostServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sectionId = req.getParameter("section");
        req.getRequestDispatcher("/WEB-INF/views/section/create-post.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getAttribute("errors") != null) {
            doGet(req, resp);
            return;
        }

        List<String> errors = new ArrayList<>();

        String sectionId = req.getParameter("section");
        String title = req.getParameter("title");
        String type = req.getParameter("type");
        String content = req.getParameter("content");
        Part picture = req.getPart("picture");

        System.out.println(sectionId);

        Map<Integer, Section> sections = (Map<Integer, Section>) getServletContext().getAttribute("sections");
        Section section = null;
        if(sectionId == null || !InputValidator.assertInt(sectionId))
            errors.add("Specificare una sezione");
        else if(null == (section = sections.get(Integer.parseInt(sectionId))))
            errors.add("La sezione specificata non esiste");

        if(title == null || title.isBlank())
            errors.add("Specificare un titolo");


        Post.Type typeEnum = null;
        if(type == null) {
            errors.add("Specificare il tipo di post");
        }
        else if(type.equalsIgnoreCase("text")) {
            typeEnum = Post.Type.TEXT;
            if (content == null)
                errors.add("Specificare il contenuto del post");
        }
        else if(type.equalsIgnoreCase("picture")) {
            typeEnum = Post.Type.IMG;
            if (picture == null || picture.getSize() <= 0){
                errors.add("Caricare un'immagine");
            } else {
                if (picture.getSize() > 5 * 1024 * 1024)
                    errors.add("Il file non deve superare i 5MB");
                String mimeType = getServletContext().getMimeType(picture.getSubmittedFileName());
                if (mimeType == null || !mimeType.startsWith("image/"))
                    errors.add("Il file caricato non è un'immagine");
            }
        }

        if(!errors.isEmpty()){
            ErrorForwarder.sendError(req, resp, errors, 400, "/newpost");
            return;
        }

        Post post = new Post();
        post.setAuthor((User) req.getAttribute("loggedUser"));
        post.setTitle(title);
        post.setSection(section);
        post.setType(typeEnum);

        if(post.getType() == Post.Type.TEXT){
            post.setContent(content);
        } else if (post.getType() == Post.Type.IMG) {
            post.setContent(FileUtils.generateFileName(picture));
        }

        int newPostId;
        if(post.getType() == Post.Type.TEXT){
            try(Connection con = ConPool.getConnection()){
                PostDAO service = new PostDAO(con);
                newPostId = service.insert(post);
            } catch (SQLException e) {
                throw new ServletException(e);
            }
        } else {
            /* Autocommit a false così un fail del filesystem impedisce l'inserimento nel db.
               Doppio try-catch per poter chiamare setAutoCommit.
            */
            try(Connection con = ConPool.getConnection()){
                con.setAutoCommit(false);
                try(InputStream fileStream = picture.getInputStream()){
                    PostDAO service = new PostDAO(con);
                    newPostId = service.insert(post);

                    String uploadRoot = FileServlet.BASE_PATH;
                    File file = new File(uploadRoot + post.getContent());
                    if(!file.getParentFile().exists())
                        file.getParentFile().mkdir();
                    Files.copy(fileStream, file.toPath());
                } catch(SQLException | IOException e){
                    con.rollback();
                    throw new ServletException(e);
                } finally {
                    con.setAutoCommit(true);
                }
            } catch (SQLException e2) {
                throw new ServletException(e2);
            }
        }

        resp.sendRedirect(getServletContext().getContextPath() + "/post?id=" + newPostId);
    }
}
