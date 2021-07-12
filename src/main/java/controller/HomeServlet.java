package controller;

import model.section.Section;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<Integer, Section> sections = (Map<Integer, Section>) getServletContext().getAttribute("sections");
        List<Section> topSections = sections.values().stream()
                .sorted(Comparator.comparingInt(Section::getnFollowersTotal).reversed())
                .collect(Collectors.toUnmodifiableList());
        req.setAttribute("topSections", topSections);

        List<Section> trendingSections = sections.values().stream()
                .sorted(Comparator.comparingInt(Section::getnFollowersWeekly).reversed())
                .collect(Collectors.toUnmodifiableList());
        req.setAttribute("trendingSections", trendingSections);

        req.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       doGet(req,resp);
    }
}

