package controller;

import section.Section;
import user.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

@WebServlet("/follow")
public class Follow extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ConcurrentMap<Integer, Section> sections =
                (ConcurrentMap<Integer,Section>) getServletContext().getAttribute("sections");
        try{
            int sectionId = Integer.parseInt(req.getParameter("section"));
            if(!sections.containsKey(sectionId)) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            HttpSession session = req.getSession(true);

            User user = (User) req.getAttribute("loggedUser");
            if(user != null){

            }

            Set<Integer> follows = (Set<Integer>) session.getAttribute("follows");
            follows.add(sectionId);
        } catch (NumberFormatException e){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
