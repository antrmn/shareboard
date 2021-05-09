package post;


import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class PostMapper {

    private Post p;
    private ResultSet rs;

    private final Map<String, SQL_Consumer<String>> map = Map.of(
            "id",             x -> p.setId(rs.getInt(x)),
            "title",          x -> p.setTitle(rs.getString(x)),
            "content",        x -> p.setContent(rs.getString(x)),
            "type",           x -> p.setType(Post.Type.valueOf(rs.getString(x))),
            "creation_date",  x -> p.setCreationDate(rs.getDate(x)),
            "votes",          x -> p.setVotes(rs.getInt(x)),
            "n_comments",     x -> p.setnComments(rs.getInt(x)),
            "vote",           x -> p.setVote(rs.getInt(x))
    );

    @FunctionalInterface
    private interface SQL_Consumer<T>{
        public void accept(T t) throws SQLException;
    }

    private PostMapper(Post p, ResultSet rs){
        this.p = p;
        this.rs = rs;
    }

    public static Post toBean(ResultSet rs) throws SQLException{
        PostMapper pm = new PostMapper(new Post(), rs);
        ResultSetMetaData rsmd = rs.getMetaData();
        Post p = new Post();

        for(int i=1; i<=rsmd.getColumnCount(); i++){
            String column = rsmd.getColumnName(i);
            SQL_Consumer<String> setter = pm.map.get(column);
            if(setter != null){
                setter.accept(column);
            }
        }
        //p.setAuthor(UserMapper.toBean(rs));
        //p.setSection(SectionMapper.toBean(rs));

        return p;
    }
}
