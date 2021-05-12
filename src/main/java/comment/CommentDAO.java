package comment;

import persistence.GenericDAO;
import util.Pair;

import java.sql.Connection;
import java.sql.Types;
import java.util.List;
import java.util.StringJoiner;

public class CommentDAO extends GenericDAO<Comment, CommentMapper> {
    protected CommentDAO(Connection con) {
        super(con,
                List.of("comment.id", "comment.content", "comment.creation_date",
                        "comment.parent_comment_id", "comment.votes", "vc1.vote",
                        "user.username AS author_username", "post.id AS post_id", "user.id AS author_id"),
                List.of("content", "author_id", "parent_comment_id", "post_id"),
                "v_comment",
                "comment",
                new CommentMapper()
                );
    }

    @Override
    protected String fillUpdateStatement(Comment comment, List<Pair<Object, Integer>> params) {
        StringJoiner valuesToSet = new StringJoiner(",");
        if(comment.getText() != null){
            params.add(new Pair<>(comment.getText(), Types.VARCHAR));
            valuesToSet.add("content=?");
        }
        if(comment.getPost() != null && comment.getPost().getId() != null){
            params.add(new Pair<>(comment.getPost().getId(), Types.INTEGER));
            valuesToSet.add("post_id=?");
        }
        if(comment.getAuthor() != null && comment.getAuthor().getId() != null){
            params.add(new Pair<>(comment.getAuthor().getId(), Types.VARCHAR));
            valuesToSet.add("author_id=?");
        }
        if(comment.getParentComment() != null && comment.getParentComment().getId() != null){
            params.add(new Pair<>(comment.getParentComment().getId(), Types.VARCHAR));
            valuesToSet.add("parent_comment_id=?");
        }
        params.add(new Pair<>(comment.getId(), Types.INTEGER));
        return valuesToSet.toString();
    }

    @Override
    protected void fillInsertStatement(Comment comment, List<Pair<Object, Integer>> params) {
        params.add(new Pair<>(comment.getText(), Types.VARCHAR));
        params.add(new Pair<>(comment.getAuthor() == null ? null : comment.getAuthor().getId(), Types.INTEGER));
        params.add(new Pair<>(comment.getParentComment() == null ? null : comment.getParentComment().getId(), Types.INTEGER));
        params.add(new Pair<>(comment.getPost() == null ? null : comment.getPost().getId(), Types.INTEGER));
    }
}