package comment;


import persistence.AbstractMapper;
import post.Post;
import user.User;

import java.util.HashMap;
import java.util.Map;


public class CommentMapper extends AbstractMapper<Comment> {
    static Map<String, SQL_TriConsumer<Comment, String>> map = new HashMap<>(){{
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

    public CommentMapper() {
        super(map);
    }

    @Override
    protected Comment instantiate() {
        Comment c = new Comment();
        c.setParentComment(new Comment());
        c.setPost(new Post());
        c.setAuthor(new User());
        return c;
    }
}
