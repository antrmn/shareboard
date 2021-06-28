package controller.util;

import javax.servlet.http.Part;
import java.nio.file.Paths;
import java.util.UUID;

public final class FileUtils {
    public static String generateFileName(Part part){
        String oldFileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
        String[] oldFileNameSplit = oldFileName.split("\\.");
        String oldFileNameExtension = oldFileNameSplit[oldFileNameSplit.length-1];
        String newFileName = UUID.randomUUID().toString() + "." + oldFileNameExtension;
        return newFileName;
    }
}
