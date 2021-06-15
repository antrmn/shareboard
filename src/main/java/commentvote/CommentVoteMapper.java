package commentvote;

import persistence.AbstractMapper;
import persistence.SQL_TriConsumer;
import comment.Comment;
import user.User;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class CommentVoteMapper implements AbstractMapper<CommentVote> {

    static LinkedHashMap<String, SQL_TriConsumer<User>> mapUser = new LinkedHashMap<>(){{
        put("user_id",        (u,s,rs) -> u.setId(rs.getInt(s)));
        put("username",       (u,s,rs) -> u.setUsername(rs.getString(s)));
    }};

    static LinkedHashMap<String, SQL_TriConsumer<Comment>> mapComment = new LinkedHashMap<>(){{
        put("comment_id",        (p,s,rs) -> p.setId(rs.getInt(s)));
    }};

    static LinkedHashMap<String, SQL_TriConsumer<CommentVote>> mapCommentVote = new LinkedHashMap<>(){{
        put("vote",     (pv,s,rs) -> pv.setVote(rs.getShort(s)));
    }};

    public List<CommentVote> toBeans(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();

        ArrayList<String> columns = new ArrayList<>();
        for (int i = 1; i <= rsmd.getColumnCount(); i++){
            columns.add(rsmd.getColumnLabel(i));
        }

        List<CommentVote> beans = new ArrayList<>();
        HashMap<Integer, User> users = new HashMap<>();
        HashMap<Integer, Comment> comments = new HashMap<>();
        while (rs.next()) {
            CommentVote commentVote = new CommentVote();

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
            commentVote.setUser(user);

            Comment comment = null;
            if (columns.contains("comment_id")) {
                comment = comments.get(rs.getInt("comment_id"));
                if (comment == null) {
                    comment = new Comment();
                    for (String column : columns) {
                        SQL_TriConsumer<Comment> consumer = mapComment.get(column);
                        if(consumer != null)
                            consumer.accept(comment, column, rs);
                    }
                    comments.put(comment.getId(), comment);
                }
            }
            commentVote.setComment(comment);

            for (String column : columns){
                SQL_TriConsumer<CommentVote> consumer = mapCommentVote.get(column);
                if(consumer != null)
                    consumer.accept(commentVote, column, rs);
            }

            beans.add(commentVote);
        }
        return beans;
    }
}
