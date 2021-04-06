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
                    con.prepareStatement("SELECT post.post_id, post.title, post.text, post.type, post.creation_date," +
                                                "user.id, user.username, " +
                                                "category.id, category.name, " +
                                                "SUM(postvotes.vote), " +
                                                "COUNT(DISTINCT comment.id) " +
                                             "FROM post " +
                                             "INNER JOIN user ON post.author_id=user.id " +
                                             "INNER JOIN category ON post.category_id=category.id " +
                                             "LEFT JOIN postvotes ON post.post_id = postvotes.post_id " +
                                             "LEFT JOIN comment ON post.post_id = comment.post_id " +
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
                post.setN_comments(rs.getInt(11));
                list.add(post);
            }

            return list;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public Post doRetrieve(int id){
        try(Connection con = ConPool.getConnection()){
            PreparedStatement ps =
                    con.prepareStatement("SELECT post.post_id, post.title, post.text, post.type, post.creation_date, user.id, user.username, \n" +
                            "category.id, category.name, \n" +
                            "SUM(postvotes.vote) \n" +
                            "FROM post \n" +
                            "INNER JOIN user ON post.author_id=user.id \n" +
                            "INNER JOIN category ON post.category_id=category.id \n" +
                            "LEFT JOIN postvotes ON post.post_id = postvotes.post_id \n" +
                            "WHERE post.post_id=? \n" +
                            "GROUP BY post.post_id;");

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(!rs.next()){
                throw new RuntimeException("Errore");
            }

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

            PreparedStatement ps_commenti =
                    con.prepareStatement("SELECT comment.id, comment.text, comment.creation_date, " +
                                             "user.id, user.username, \n" +
                                             "SUM(commentvotes.vote)\n" +
                                             "FROM comment  \n" +
                                             "INNER JOIN user ON comment.author_id=user.id \n" +
                                             "LEFT JOIN commentvotes ON comment.id = commentvotes.comment_id \n" +
                                             "WHERE comment.post_id=? \n" +
                                             "GROUP BY comment.id; "); //Group by necessario per non avere record
                                                                       // col solo valore "voti"

            ps_commenti.setInt(1,id);

            ResultSet rs_commenti = ps_commenti.executeQuery();
            List<Comment> list = new ArrayList<>();
            while(rs_commenti.next()){
                Comment c = new Comment();
                c.setId(rs_commenti.getInt(1));
                c.setText(rs_commenti.getString(2));
                c.setCreationDate(rs_commenti.getDate(3));
                User u = new User();
                u.setId(rs_commenti.getInt(4));
                u.setUsername(rs_commenti.getString(5));
                c.setAuthor(u);
                c.setVotes(rs_commenti.getInt(6));
                list.add(c);
            }

            post.setN_comments(list.size());
            post.setComments(list);
            return post;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}


