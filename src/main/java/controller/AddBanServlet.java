package controller;

import com.google.gson.Gson;
import model.ban.Ban;
import model.ban.BanDAO;
import model.persistence.ConPool;
import model.section.Section;
import model.user.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/admin/addban")
@MultipartConfig
public class AddBanServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<String> errors = new ArrayList<>();

        String date = req.getParameter("endDate");
        int userId = Integer.parseInt(req.getParameter("userId"));
        int sectionId = Integer.parseInt(req.getParameter("sectionId"));
        Instant endDate = Instant.now();
        Instant currentDate = Instant.now();
        HttpSession session = req.getSession(true);
        int adminId = (Integer)session.getAttribute("loggedUserId");

        if (date!= null && !date.isEmpty()){
            endDate = LocalDate.parse(date).atStartOfDay().toInstant(ZoneOffset.UTC);

            if(currentDate.isAfter(endDate)){
                errors.add("La data non può essere antecendete a quella attuale");
            }
        } else{
            errors.add("Data non valida");
        }


        if(!errors.isEmpty()){
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            Gson gson = new Gson();
            resp.getWriter().print(gson.toJson(errors));
            resp.getWriter().flush();
            return;
        }

        try(Connection con = ConPool.getConnection()){
            Ban b = new Ban();
            if (sectionId != -1){
                Section s = new Section();
                s.setId(sectionId);
                b.setSection(s);
                b.setGlobal(false);
            } else{
                b.setGlobal(true);
            }
            b.setStartTime(currentDate);
            b.setEndTime(endDate);
            User u = new User();
            u.setId(userId);
            b.setUser(u);
            User admin = new User();
            admin.setId(adminId);
            b.setAdmin(admin);
            BanDAO service = new BanDAO(con);
            service.insert(b);
        } catch (SQLException e) {
            throw new ServletException(e);
        }

    }
}