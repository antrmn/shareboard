package controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public interface ErrorHandler {

    default void authenticate(HttpSession session) throws InvalidRequestException {
        if(session == null || session.getAttribute("accountSession") == null){
            throw new InvalidRequestException("errore autenticazione", List.of("Non sei autenticato"), HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    default void authorize(HttpSession session) throws InvalidRequestException {
        authenticate(session);
       // AccountSession ac = (AccountSession) session.getAttribute("accountSession");

//        if(!accountSession.isAdmin()){
//            throw new InvalidRequestException("Errore autorizzazione", List.of("Azione non consentita"), HttpServletResponse.SC_FORBIDDEN);
//        }
    }

    default void internalError() throws InvalidRequestException{
        List<String> errors = List.of("ERRORE", "Riprova più tardi");
        throw new InvalidRequestException("Errore interno", errors, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    default void notFound() throws InvalidRequestException{
        throw new InvalidRequestException("Errore interno", List.of("Not Found"), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    default void notAllowed() throws InvalidRequestException{
        throw new InvalidRequestException("Azione non consentita", List.of("Azione non consentita"), HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }
}
