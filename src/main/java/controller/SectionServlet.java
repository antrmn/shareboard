package controller;

import model.section.Section;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@WebServlet("/s")
public class SectionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sectionName = req.getParameter("section");
        if(sectionName == null || sectionName.isBlank()){
            resp.sendRedirect(getServletContext().getContextPath());
            return;
        }

        Map<Integer, Section> sections = (Map<Integer, Section>) req.getServletContext().getAttribute("sections");
        Optional<Section> section = sections.values()
                                            .stream()
                                             .filter(x -> x.getName().equalsIgnoreCase(sectionName))
                                            .findFirst();

        if(section.isEmpty()) {
            resp.sendRedirect(getServletContext().getContextPath());
            return;
        }
        req.setAttribute("section", section.get());
        req.getRequestDispatcher("/WEB-INF/views/section/section.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}

