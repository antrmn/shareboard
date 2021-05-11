package comment;


import post.Post;
import post.PostMapper;
import user.UserMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;


public class CommentMapper {

    @FunctionalInterface
    private interface SQL_Consumer<T>{
        void accept(T t) throws SQLException;
    }

    private Comment c;
    private ResultSet rs;

    private final Map<String, SQL_Consumer<String>> map = Map.of(
            "comment_id",                x -> c.setId(rs.getInt(x)),
            "comment_content",           x -> c.setText(rs.getString(x)),
            "comment_creation_date",     x -> c.setCreationDate(rs.getTimestamp(x).toInstant()),
            "parent_comment_id",         x -> c.getParentComment().setId(rs.getInt(x)),
            "comment_votes",             x -> c.setVotes(rs.getInt(x)),
            "comment_vote",              x -> c.setVote(rs.getInt(x))
    );

    private CommentMapper(Comment c, ResultSet rs){
        this.c = c;
        this.rs = rs;
    }

    public static Comment toBean(ResultSet rs) throws SQLException{
        CommentMapper cm = new CommentMapper(new Comment(), rs);
        ResultSetMetaData rsmd = rs.getMetaData();
        cm.c.setParentComment(new Comment());

        for(int i=1; i<=rsmd.getColumnCount(); i++){
            String column = rsmd.getColumnLabel(i);
            SQL_Consumer<String> setter = cm.map.get(column);
            if(setter != null){
                setter.accept(column);
            }
        }

        cm.c.setAuthor(UserMapper.toBean(rs));
        cm.c.setPost(PostMapper.toBean(rs));

        return cm.c;
    }
}
