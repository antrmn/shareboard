package controller;


import model.persistence.ConPool;
import model.section.Section;
import model.section.SectionDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/admin/*")
public class Admin extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = (req.getPathInfo() != null) ? req.getPathInfo() : "/";
        System.out.print(path);
        switch(path){
            case "/create-section":
                req.getRequestDispatcher("/WEB-INF/views/crm/create-section.jsp").forward(req,resp);
                break;
            case "/user-panel":
                //temp
                req.getRequestDispatcher("/WEB-INF/views/crm/admin.jsp").forward(req,resp);
                break;
            default:
                req.getRequestDispatcher("/WEB-INF/views/crm/admin.jsp").forward(req,resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = (req.getPathInfo() != null) ? req.getPathInfo() : "/";
        System.out.println(path);
        switch(path) {
            case "/create-section":
                //authenticate
                System.out.println("PROVA");
                try (Connection con = ConPool.getConnection()){
                    SectionDAO service = new SectionDAO(con);
                    List<Section> sections = new ArrayList<Section>();
                    Section s = new Section();
                    s.setDescription("test");
                    s.setName("test");
                    s.setPicture("test");
                    //s.setPicture();
                    sections.add(s);
                    service.insert(sections);
                    req.getRequestDispatcher("/WEB-INF/views/crm/create-section.jsp").forward(req, resp);

                } catch(SQLException  e){
                    e.printStackTrace();
                    req.getRequestDispatcher("/WEB-INF/views/crm/create-section.jsp").forward(req, resp);
                }
                break;
        }
    }
}