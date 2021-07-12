package controller;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import model.persistence.ConPool;
import model.post.PostDAO;
import model.user.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/crm/admin.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<LocalDate, Integer> recentRegistrations = new HashMap<>();
        Map<String, Integer> postBySection = new HashMap<>();
        Map<LocalDate, Integer> recentPosts = new HashMap<>();

        try(Connection con = ConPool.getConnection()){
            UserDAO service = new UserDAO(con);
            PostDAO postService = new PostDAO(con);
            Instant currentTime = Instant.now();
            for(int i = 0; i<5; i++){
                LocalDate date = currentTime.minus(i, ChronoUnit.DAYS).atOffset(ZoneOffset.UTC).toLocalDate();
//                System.out.println(date.getDayOfMonth());
                recentRegistrations.put(date, 0);
                recentPosts.put(date, 0);
            }

            Map<Instant, Integer> registrations = service.getRegistrationsinRange();
            Map<Instant, Integer> postsByDate = postService.getPostCountByDays();
            postBySection = postService.getPostCountBySection();
            for (Map.Entry<Instant, Integer> entry : registrations.entrySet()) {
                LocalDate date = entry.getKey().atOffset(ZoneOffset.UTC).toLocalDate();
                if (recentRegistrations.containsKey(date)){
                    recentRegistrations.put(date, entry.getValue());
                }
            }

            for (Map.Entry<Instant, Integer> entry : postsByDate.entrySet()) {
                LocalDate date = entry.getKey().atOffset(ZoneOffset.UTC).toLocalDate();
                if (recentPosts.containsKey(date)){
//                    System.out.println(date.getDayOfMonth());
                    recentPosts.put(date, entry.getValue());
                }
            }

//            for (Map.Entry<LocalDate, Integer> entry : recentRegistrations.entrySet()) {
//                System.out.println(entry.getKey() + "/" + entry.getValue());
//            }



        } catch (SQLException throwables) {
            throw new ServletException(throwables);
        }

        JsonObject json=new JsonObject();
        Gson g = new Gson();
        json.addProperty("section_data", g.toJson(req.getServletContext().getAttribute("sections")));
        json.addProperty("registration_data", g.toJson(recentRegistrations));
        json.addProperty("post_bysection_data", g.toJson(postBySection));
        json.addProperty("post_recent_data", g.toJson(recentPosts));
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().print(json);
        resp.getWriter().flush();
    }
}