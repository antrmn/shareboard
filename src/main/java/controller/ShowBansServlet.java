package controller;

import model.ban.Ban;
import model.ban.BanDAO;
import model.ban.BanSpecificationBuilder;
import model.persistence.ConPool;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


@WebServlet("/admin/showbans")
public class ShowBansServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int userId = Integer.parseInt(req.getParameter("userId"));
        try (Connection con = ConPool.getConnection()){
            BanDAO service = new BanDAO(con);
            BanSpecificationBuilder bsb = new BanSpecificationBuilder().byUserId(userId);
            List<Ban> bans = service.fetch(bsb.build());

            req.setAttribute("bans", bans);
            req.setAttribute("userId", userId);
            req.getRequestDispatcher("/WEB-INF/views/crm/show-bans.jsp").forward(req, resp);
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
}
