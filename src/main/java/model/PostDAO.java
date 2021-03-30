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
            PreparedStatement ps =
                    con.prepareStatement("SELECT post_id, title, text, type, creation_date FROM post");
            ResultSet rs = ps.executeQuery();
            List<Post> list = new ArrayList<>();
            while(rs.next()){
                Post post = new Post();
                post.setId(rs.getInt(1));
                post.setTitle(rs.getString(2));
                post.setText(rs.getString(3));
                post.setType(Post.Type.valueOf(rs.getString(4)));
                post.setCreationDate(rs.getDate(5));
                list.add(post);
            }
            return list;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}


