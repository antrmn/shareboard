package controller;

import controller.util.ErrorForwarder;
import controller.util.FileUtils;
import controller.util.InputValidator;
import model.persistence.ConPool;
import model.user.HashedPassword;
import model.user.User;
import model.user.UserDAO;

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

@WebServlet("/edituser")
@MultipartConfig
public class EditUserServlet extends HttpServlet {
    /* controlla:
        - se l'id messo nel query string è valido
        - se il post esiste
        - se l'utente ha i permessi per editarlo (in quanto utente loggato o admin)
        Se i controlli hanno esito positivo restituisce true e setta l'utente come request attribute SOLO se non è già presente
     */
    private boolean check(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String _userId = req.getParameter("id");
        if(_userId == null || _userId.isBlank() || !InputValidator.assertInt(_userId)){
            ErrorForwarder.sendError(req, resp, List.of("User id non valido"), 400);
            return false;
        }
        int userId = Integer.parseInt(_userId);

        User loggedUser = (User) req.getAttribute("loggedUser");
        User user = null;
        try(Connection con = ConPool.getConnection()){
            UserDAO service = new UserDAO(con);
            user = service.get(userId, false);
        } catch (SQLException throwables) {
            throw new ServletException(throwables);
        }

        if(user == null){
            ErrorForwarder.sendError(req, resp, List.of("L'utente non esiste"), 400);
            return false;
        }
        if(!user.getId().equals(loggedUser.getId())
                && loggedUser.getAdmin().equals(false)){
            ErrorForwarder.sendError(req, resp, List.of(""), HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
        if(req.getAttribute("user") == null)
            req.setAttribute("user", user);
        return true;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(!check(req, resp))
            return;
        req.getRequestDispatcher("/WEB-INF/views/edit-user.jsp").forward(req, resp);
    }


    /*
     * se email è null o blank: ignora
     * se description è null: ignora
     * se picture è null o size=0: ignora
     * se pass è null o empty (non blank!): ignora
     *
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getAttribute("errors") != null) {
            doGet(req, resp);
            return;
        }

        if (!check(req, resp))
            return;

        List<String> errors = new ArrayList<>();

        User userToEdit = (User) req.getAttribute("user");
        String oldPicture = userToEdit.getPicture();
        userToEdit.setPicture(null); //ignora campo picture se resta null

        String email = req.getParameter("email");
        String description = req.getParameter("description");
        Part picture = req.getPart("picture");
        String pass = req.getParameter("pass");
        String pass2 = req.getParameter("pass2");

        if (email != null && !email.isBlank())
            userToEdit.setEmail(email);

        if(description != null)
            userToEdit.setDescription(description);

        if(pass != null && !pass.isEmpty()){
            if(!(pass.length()>=3 && pass.length()<=255)){
                errors.add("La password deve essere di lunghezza compresa tra i 3 e i 255 caratteri");
            } else {
                if(!pass.equals(pass2))
                    errors.add("Le password devono coincidere");
                else
                    userToEdit.setPassword(HashedPassword.hash(pass));
            }
        }

        if (picture != null && picture.getSize() > 0) {
            userToEdit.setPicture(FileUtils.generateFileName(picture));
            if (picture.getSize() > 5 * 1024 * 1024)
                errors.add("Il file non deve superare i 5MB");
            String mimeType = getServletContext().getMimeType(picture.getSubmittedFileName());
            if (mimeType == null || !mimeType.startsWith("image/"))
                errors.add("Il file caricato non è un'immagine");
        }

        if(!errors.isEmpty()){
            ErrorForwarder.sendError(req, resp, errors, 400, "/edituser");
            return;
        }

        try (Connection con = ConPool.getConnection()) {
            con.setAutoCommit(false);
            try (InputStream fileStream = picture.getInputStream()) {
                UserDAO service = new UserDAO(con);
                service.update(userToEdit);

                String uploadRoot = FileServlet.BASE_PATH;
                if (userToEdit.getPicture() != null) {
                    File file = new File(uploadRoot + userToEdit.getPicture());
                    if (!file.getParentFile().exists())
                        file.getParentFile().mkdir();
                    Files.copy(fileStream, file.toPath());
                    if(oldPicture != null){
                        Files.deleteIfExists(Path.of(FileServlet.BASE_PATH + File.separator + oldPicture));
                    }
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

        resp.sendRedirect(req.getContextPath() + "/u/" + userToEdit.getUsername());
    }
}
