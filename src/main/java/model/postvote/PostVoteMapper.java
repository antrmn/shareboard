package model.postvote;

import model.persistence.AbstractMapper;
import model.persistence.SQL_TriConsumer;
import model.post.Post;
import model.user.User;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class PostVoteMapper implements AbstractMapper<PostVote> {

    static LinkedHashMap<String, SQL_TriConsumer<User>> mapUser = new LinkedHashMap<>(){{
        put("user_id",        (u,s,rs) -> u.setId(rs.getInt(s)));
        put("username",       (u,s,rs) -> u.setUsername(rs.getString(s)));
    }};

    static LinkedHashMap<String, SQL_TriConsumer<Post>> mapPost = new LinkedHashMap<>(){{
        put("post_id",        (p,s,rs) -> p.setId(rs.getInt(s)));
        put("post_title",     (p,s,rs) -> p.setTitle(rs.getString(s)));
    }};

    static LinkedHashMap<String, SQL_TriConsumer<PostVote>> mapPostVote = new LinkedHashMap<>(){{
        put("vote",     (pv,s,rs) -> pv.setVote(rs.getShort(s)));
    }};

    public List<PostVote> toBeans(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();

        ArrayList<String> columns = new ArrayList<>();
        for (int i = 1; i <= rsmd.getColumnCount(); i++){
            columns.add(rsmd.getColumnLabel(i));
        }

        List<PostVote> beans = new ArrayList<>();
        HashMap<Integer, User> users = new HashMap<>();
        HashMap<Integer, Post> posts = new HashMap<>();
        while (rs.next()) {
            PostVote postVote = new PostVote();

            User user = null;
            if (columns.contains("user_id")) {
                user = users.get(rs.getInt("user_id"));
                if (user == null) {
                    user = new User();
                    for (String column : columns) {
                        SQL_TriConsumer<User> consumer = mapUser.get(column);
                        if(consumer != null)
                            consumer.accept(user, column, rs);
                    }
                    users.put(user.getId(), user);
                }
            }
            postVote.setUser(user);

            Post post = null;
            if (columns.contains("post_id")) {
                post = posts.get(rs.getInt("post_id"));
                if (post == null) {
                    post = new Post();
                    for (String column : columns) {
                        SQL_TriConsumer<Post> consumer = mapPost.get(column);
                        if(consumer != null)
                            consumer.accept(post, column, rs);
                    }
                    posts.put(post.getId(), post);
                }
            }
            postVote.setPost(post);

            for (String column : columns){
                SQL_TriConsumer<PostVote> consumer = mapPostVote.get(column);
                if(consumer != null)
                    consumer.accept(postVote, column, rs);
            }

            beans.add(postVote);
        }
        return beans;
    }
}
