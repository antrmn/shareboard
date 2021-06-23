package controller;

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
import java.nio.file.Paths;

@WebServlet("/newpost")
@MultipartConfig
public class NewPostServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /* picture corrisponde all'attributo "name" di <input> */
        Part filePart = req.getPart("picture");

        /* il metodo filePart.getSubmittedFileName() ha un comportamento inconsistente:
        * alcuni browser fanno sì che questo metodo restituisca il filename, altri invece fanno sì
        * che questo metodo restituisca il path completo.
        * Per assicurarci di avere sempre il fileName (Quindi non il path completo) usiamo il
        * metodo Paths.get(...).getFileName() */
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        System.out.println(filePart.getSubmittedFileName() + " ricevuto.");

        /* I file vengono caricati qui */
        String uploadRoot = FileServlet.BASE_PATH;
        System.out.println("Upload Root: " + uploadRoot);

        /* Inizializzo stream da filePart */
        try(InputStream fileStream = filePart.getInputStream()){
            File file = new File(uploadRoot + fileName);

            /* Se la cartella "upload" non esiste la creo */
            if(!file.getParentFile().exists())
                file.getParentFile().mkdir();

            /* Copio il flusso binario dello stream nel file appena creato */
            Files.copy(fileStream, file.toPath());
        }

        /* PER TESTARE CON curl (Su PowerShell):
            > cd **directory**
            > curl -X POST -v --form picture=@NOMEIMMAGINE.png localhost:8080/shareboard/newpost


           PER TESTARE CON FORM HTML:
                <form method="post" enctype="multipart/form-data" action="${pageContext.request.contextPath}/newpost">
                    <input type="file" name="picture">
                    <input type="submit">
                </form>
         */



    }
}
