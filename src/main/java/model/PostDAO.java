package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostDAO {

    public List<Post> doRetrieveAll(){
        try(Connection con = ConPool.getConnection()){

            PreparedStatement ps = con.prepareStatement("SELECT post.post_id, post.title, post.text, post.type, post.creation_date, user.id, user.username, category.id, category.name, SUM(postvotes.vote)\n" +
                                                            "FROM post\n" +
                                                            "LEFT JOIN user ON post.author_id=user.id\n" +
                                                            "INNER JOIN category ON post.category_id=category.id \n" +
                                                            "LEFT JOIN postvotes ON post.post_id = postvotes.post_id\n" +
                                                            "GROUP BY post.post_id;");

            ResultSet rs = ps.executeQuery();
            List<Post> list = new ArrayList<>();
            while(rs.next()){
                Post post = new Post();
                post.setId(rs.getInt(1));
                post.setTitle(rs.getString(2));
                post.setText(rs.getString(3));
                post.setType(Post.Type.valueOf(rs.getString(4)));
                post.setCreationDate(rs.getDate(5));
                User user = new User();
                user.setId(rs.getInt(6));
                user.setUsername(rs.getString(7));
                post.setAuthor(user);
                Category category = new Category();
                category.setId(rs.getInt(8));
                category.setName(rs.getString(9));
                post.setCategory(category);
                post.setVoti(rs.getInt(10));
                list.add(post);
            }

            return list;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}


