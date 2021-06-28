package controller;

import controller.util.ErrorForwarder;
import controller.util.FileUtils;
import controller.util.InputValidator;
import model.persistence.ConPool;
import model.post.Post;
import model.post.PostDAO;
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
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/editpost")
@MultipartConfig
public class EditPostServlet extends HttpServlet {
    /* controlla:
        - se l'id messo nel query string è valido
        - se il post esiste
        - se l'utente ha i permessi per editarlo (in quanto autore o admin)
        Se i controlli hanno esito positivo restituisce true e setta il post come request attribute SOLO se non è già presente
     */
    private boolean check(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String _postId = req.getParameter("id");
        if(_postId == null || _postId.isBlank() || !InputValidator.assertInt(_postId)){
            ErrorForwarder.sendError(req, resp, List.of("Post id non valido"), 400);
            return false;
        }
        int postId = Integer.parseInt(_postId);

        User loggedUser = (User) req.getAttribute("loggedUser");
        Post post = null;
        try(Connection con = ConPool.getConnection()){
            PostDAO service = new PostDAO(con);
            post = service.get(postId);
        } catch (SQLException throwables) {
            throw new ServletException(throwables);
        }

        if(post == null){
            ErrorForwarder.sendError(req, resp, List.of("Il post non esiste"), 400);
            return false;
        }
        if(!post.getAuthor().getId().equals(loggedUser.getId())
                && loggedUser.getAdmin().equals(false)){
            ErrorForwarder.sendError(req, resp, List.of(""), HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
        if(req.getAttribute("post") == null)
            req.setAttribute("post", post);
        return true;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(!check(req, resp))
            return;
        req.getRequestDispatcher("/WEB-INF/views/section/edit-post.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getAttribute("errors") != null) {
            doGet(req, resp);
            return;
        }

        if (!check(req, resp))
            return;

        List<String> errors = new ArrayList<>();

        Post postToEdit = (Post) req.getAttribute("post");
        Post.Type oldType = postToEdit.getType();

        String title = req.getParameter("title");
        String type = req.getParameter("type");
        String content = req.getParameter("content");
        Part picture = req.getPart("picture");

        if (title == null || title.isBlank())
            errors.add("Specificare un titolo");

        if (type != null && type.equalsIgnoreCase("text")) {
            postToEdit.setType(Post.Type.TEXT);
        } else if (type != null && type.equalsIgnoreCase("picture")) {
            postToEdit.setType(Post.Type.IMG);
        }

        if (postToEdit.getType() != oldType) {
            if (postToEdit.getType() == Post.Type.TEXT && content == null)
                errors.add("Specificare il contenuto del post");
            if (postToEdit.getType() == Post.Type.IMG && (picture == null || picture.getSize() <= 0))
                errors.add("Caricare un'immagine");
        }

        if (picture != null && picture.getSize() > 0) {
            if (picture.getSize() > 5 * 1024 * 1024)
                errors.add("Il file non deve superare i 5MB");
            String mimeType = getServletContext().getMimeType(picture.getSubmittedFileName());
            if (mimeType == null || !mimeType.startsWith("image/"))
                errors.add("Il file caricato non è un'immagine");
        }

        if (!errors.isEmpty()) {
            ErrorForwarder.sendError(req, resp, errors, 400, "/editpost");
            return;
        }

        postToEdit.setTitle(title);

        String oldContent = postToEdit.getContent();
        if (postToEdit.getType() == Post.Type.TEXT) {
            postToEdit.setContent(content);
        } else if (postToEdit.getType() == Post.Type.IMG) {
            if (picture == null) {
                postToEdit.setContent(null);
            } else {
                postToEdit.setContent(FileUtils.generateFileName(picture));
            }
        }

        if (postToEdit.getType() == Post.Type.TEXT) {
            try (Connection con = ConPool.getConnection()) {
                PostDAO service = new PostDAO(con);
                service.update(postToEdit);
            } catch (SQLException throwables) {
                throw new ServletException(throwables);
            }
        } else {
            try (Connection con = ConPool.getConnection()) {
                con.setAutoCommit(false);
                try (InputStream fileStream = picture.getInputStream()) {
                    PostDAO service = new PostDAO(con);
                    service.update(postToEdit);

                    String uploadRoot = FileServlet.BASE_PATH;
                    if (postToEdit.getContent() != null) {
                        File file = new File(uploadRoot + postToEdit.getContent());
                        if (!file.getParentFile().exists())
                            file.getParentFile().mkdir();
                        Files.copy(fileStream, file.toPath());
                    }
                } catch (SQLException | IOException e) {
                    con.rollback();
                    throw new ServletException(e);
                } finally {
                    con.setAutoCommit(true);
                }
            } catch (SQLException e2) {
                throw new ServletException(e2);
            }
        }

        if(postToEdit.getType() != oldType){
            Files.deleteIfExists(Path.of(FileServlet.BASE_PATH + File.separator + oldContent));
        }
        resp.sendRedirect(getServletContext().getContextPath() + "/post?id=" + postToEdit.getId());
    }
}
