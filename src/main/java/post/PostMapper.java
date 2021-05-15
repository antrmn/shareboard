package post;

import persistence.AbstractMapper;
import persistence.SQL_TriConsumer;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostMapper implements AbstractMapper<Post> {
    static HashMap<String, SQL_TriConsumer<Post>> map = new HashMap<>(){{
             put("id",             (p,s,rs) -> p.setId(rs.getInt(s)));
             put("title",          (p,s,rs) -> p.setTitle(rs.getString(s)));
             put("content",        (p,s,rs) -> p.setContent(rs.getString(s)));
             put("type",           (p,s,rs) -> p.setType(Post.Type.valueOf(rs.getString(s))));
             put("creation_date",  (p,s,rs) -> p.setCreationDate(rs.getTimestamp(s).toInstant()));
             put("votes",          (p,s,rs) -> p.setVotes(rs.getInt(s)));
             put("n_comments",     (p,s,rs) -> p.setnComments(rs.getInt(s)));
             put("vote",           (p,s,rs) -> p.setVote(rs.getInt(s)));
             put("author_id",      (p,s,rs) -> p.getAuthor().setId(rs.getInt(s)));
             put("author_username",(p,s,rs) -> p.getAuthor().setUsername(rs.getString(s)));
             put("is_admin",       (p,s,rs) -> p.getAuthor().setAdmin(rs.getBoolean(s)));
             put("section_id",     (p,s,rs) -> p.getSection().setId(rs.getInt(s)));
             put("section_name",   (p,s,rs) -> p.getSection().setName(rs.getString(s)));
    }};

    public List<Post> toBeans(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        List<Post> beans = new ArrayList<>();

        while (rs.next()) {
            Post bean = new Post();
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                String column = rsmd.getColumnLabel(i);
                SQL_TriConsumer<Post> setter = map.get(column);
                if (setter != null) {
                    setter.accept(bean, column, rs);
                }
            }
            beans.add(bean);
        }
        return beans;
    }
}
