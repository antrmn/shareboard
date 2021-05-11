package post;


import section.SectionMapper;
import user.UserMapper;

import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;


public class PostMapper {

    @FunctionalInterface
    private interface SQL_Consumer<T>{
        void accept(T t) throws SQLException;
    }

    private Post p;
    private ResultSet rs;

    private final Map<String, SQL_Consumer<String>> map = Map.of(
            "post_id",             x -> p.setId(rs.getInt(x)),
            "post_title",          x -> p.setTitle(rs.getString(x)),
            "post_content",        x -> p.setContent(rs.getString(x)),
            "post_type",           x -> p.setType(Post.Type.valueOf(rs.getString(x))),
            "post_creation_date",  x -> p.setCreationDate(rs.getTimestamp(x).toInstant()),
            "post_votes",          x -> p.setVotes(rs.getInt(x)),
            "post_n_comments",     x -> p.setnComments(rs.getInt(x)),
            "post_vote",           x -> p.setVote(rs.getInt(x))
    );

    private PostMapper(Post p, ResultSet rs){
        this.p = p;
        this.rs = rs;
    }

    public static Post toBean(ResultSet rs) throws SQLException{
        PostMapper pm = new PostMapper(new Post(), rs);
        ResultSetMetaData rsmd = rs.getMetaData();

        for(int i=1; i<=rsmd.getColumnCount(); i++){
            String column = rsmd.getColumnLabel(i);
            SQL_Consumer<String> setter = pm.map.get(column);
            if(setter != null){
                setter.accept(column);
            }
        }

        pm.p.setAuthor(UserMapper.toBean(rs));
        pm.p.setSection(SectionMapper.toBean(rs));

        return pm.p;
    }
}
