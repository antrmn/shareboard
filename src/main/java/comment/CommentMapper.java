package comment;


import persistence.AbstractMapper;
import persistence.SQL_TriConsumer;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CommentMapper implements AbstractMapper<Comment> {
    static Map<String, SQL_TriConsumer<Comment>> map = new HashMap<>(){{
        map.put("id",                (c,s,rs) -> c.setId(rs.getInt(s)));
        map.put("content",           (c,s,rs) -> c.setText(rs.getString(s)));
        map.put("creation_date",     (c,s,rs) -> c.setCreationDate(rs.getTimestamp(s).toInstant()));
        map.put("parent_comment_id", (c,s,rs) -> c.getParentComment().setId(rs.getInt(s)));
        map.put("votes",             (c,s,rs) -> c.setVotes(rs.getInt(s)));
        map.put("vote",              (c,s,rs) -> c.setVote(rs.getInt(s)));
        map.put("author_id",         (c,s,rs) -> c.getAuthor().setId(rs.getInt(s)));
        map.put("author_username",   (c,s,rs) -> c.getAuthor().setUsername(rs.getString(s)));
        map.put("post_id",           (c,s,rs) -> c.getPost().setId(rs.getInt(s)));
        map.put("post_title",        (c,s,rs) -> c.getPost().setTitle(rs.getString(s)));
    }};

    public List<Comment> toBeans(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        List<Comment> beans = new ArrayList<>();

        while (rs.next()) {
            Comment bean = new Comment();
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                String column = rsmd.getColumnLabel(i);
                SQL_TriConsumer<Comment> setter = map.get(column);
                if (setter != null) {
                    setter.accept(bean, column, rs);
                }
            }
            beans.add(bean);
        }
        return beans;
    }

    public HashMap<Integer, ArrayList<Comment>> toBeansHierarchy(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();

        HashMap<Integer, ArrayList<Comment>> commentsMap = new HashMap<>();
        while(rs.next()) {
            Comment comment = new Comment();
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                String column = rsmd.getColumnLabel(i);
                SQL_TriConsumer<Comment> setter = map.get(column);
                if (setter != null) {
                    setter.accept(comment, column, rs);
                }
            }
            int parentCommentId = comment.getParentComment().getId(); //Ricorda: JDBC restituisce 0 se una colonna di tipo INTEGER ha valore NULL.
            if(!commentsMap.containsKey(parentCommentId)){
                commentsMap.put(parentCommentId, new ArrayList<>());
            }
            commentsMap.get(parentCommentId).add(comment);
        }
        return commentsMap;
    }
}
