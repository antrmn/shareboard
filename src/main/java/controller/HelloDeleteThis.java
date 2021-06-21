package controller;

import model.follow.Follow;
import model.follow.FollowDAO;
import model.persistence.ConPool;
import model.section.Section;
import model.user.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/test")
public class HelloDeleteThis extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       /* UserSpecificationBuilder usb = new UserSpecificationBuilder().byUsernameExact("pincopallo2").getPassword();
        try (Connection con = ConPool.getConnection()){
            UserDAO service = new UserDAO(con);
            User user = service.fetch(usb.build()).get(0);
            System.out.println(user.getPassword());
            System.out.println(user.getPassword().check("ciao"));
            System.out.println(user.getPassword().check("oiac"));
            System.out.println(user.getPassword().check("diocan"));
            System.out.println(user.getPassword().check("diocan1"));

            PostDAO pd = new PostDAO(con);
            Post post = new Post();
            post.setTitle("ciao");
            section.Section a = new section.Section();
            a.setId(2);
            post.setSection(a);
            post.setAuthor(user);
            post.setContent("abbalabba");
            post.setType(null);
            pd.insert(List.of(post));
        } catch(SQLException e){
            e.printStackTrace();
        } */

        User u1 = new User();
        u1.setId(1);
        User u2 = new User();
        u2.setId(2);
        Section s1 = new Section();
        s1.setId(1);
        Section s2 = new Section();
        s2.setId(2);

        Follow dueuno = new Follow();
        dueuno.setUser(u2);
        dueuno.setSection(s1);

        Follow unodue = new Follow();
        unodue.setUser(u1);
        unodue.setSection(s2);

        Follow duedue = new Follow();
        duedue.setUser(u2);
        duedue.setSection(s2);

        try(Connection con = ConPool.getConnection()){
            FollowDAO service = new FollowDAO(con);
            System.out.println(service.insert(List.of(dueuno, unodue, duedue)));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
