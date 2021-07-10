package controller;

import model.follow.FollowDAO;
import model.follow.FollowSpecificationBuilder;
import model.persistence.ConPool;
import model.persistence.Specification;
import model.section.Section;
import model.section.SectionDAO;
import model.section.SectionSpecificationBuilder;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@WebServlet(value = "/admin/updatesections", loadOnStartup = 0)
public class UpdateSectionsServlet extends HttpServlet {

    //Accessi a questo metodo vanno serializzati.
    public synchronized static void updateSections(ServletContext ctx) throws SQLException {
        try (Connection con = ConPool.getConnection()) {
            SectionDAO service = new SectionDAO(con);
            List<model.section.Section> _sections = service.fetch(new SectionSpecificationBuilder().sortById().build());
            Map<Integer, Section> sections
                    = _sections.stream().collect(Collectors.toUnmodifiableMap(x -> x.getId(), x -> x));

            FollowDAO service2 = new FollowDAO(con);
            for (Section section : sections.values()){
                Specification getTotal = new FollowSpecificationBuilder().bySectionId(section.getId()).build();
                section.setnFollowersTotal(service2.count(getTotal));
                Specification getWeekly = new FollowSpecificationBuilder()
                                .bySectionId(section.getId())
                                .isNewerThan(Instant.now().minus(7, ChronoUnit.DAYS)).build();
                section.setnFollowersWeekly(service2.count(getWeekly));
            }
            ctx.setAttribute("sections", sections);
        }
    }


    @Override
    public void init() throws ServletException {
        /* Imposta attività pianificata per aggiornare sezioni e la invoca immediatamente */
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            try {
                updateSections(getServletContext());
            } catch (Throwable throwables) {
                throwables.printStackTrace();
            }
        },0,10, TimeUnit.MINUTES); //10 secondi per testare
    }

    /* Aggiorna manualmente */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            updateSections(req.getServletContext());
        } catch (SQLException throwables) {
            throw new ServletException(throwables);
        }
        resp.sendRedirect(req.getContextPath() + "/admin");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}

