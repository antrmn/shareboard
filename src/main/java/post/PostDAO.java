package post;

import persistence.GenericDAO;
import util.Pair;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.StringJoiner;

public class PostDAO extends GenericDAO<Post, PostMapper> {

    public PostDAO(Connection con) throws SQLException {
        super(
                con,
                List.of("post.id", "post.title", "post.content", "post.type", "post.creation_date",
                        "user.id AS author_id", "user.username AS author_username", "is_admin", "section.id AS section_id",
                        "section.name AS section_name", "post.votes", "post.n_comments", "v1.vote"),
                List.of("title", "content", "type", "author_id", "section_id"),
                "v_post",
                "post",
                new PostMapper()
                );
    }

    @Override
    protected String fillUpdateStatement(Post post, List<Pair<Object, Integer>> params) {
        StringJoiner valuesToSet = new StringJoiner(",");
        if(post.getTitle() != null){
            params.add(new Pair<>(post.getTitle(), Types.VARCHAR));
            valuesToSet.add("title=?");
        }
        if(post.getContent() != null){
            params.add(new Pair<>(post.getContent(), Types.VARCHAR));
            valuesToSet.add("content=?");
        }
        if(post.getType() != null){
            params.add(new Pair<>(post.getType().name(), Types.VARCHAR));
            valuesToSet.add("type=?");
        }
        if(post.getSection() != null && post.getSection().getId() != null){
            params.add(new Pair<>(post.getSection().getId(), Types.INTEGER));
            valuesToSet.add("section_id=?");
        }
        if(post.getAuthor() != null && post.getAuthor().getId() != null){
            params.add(new Pair<>(post.getAuthor().getId(), Types.VARCHAR));
            valuesToSet.add("author_id=?");
        }
        params.add(new Pair<>(post.getId(), Types.INTEGER));
        return valuesToSet.toString();
    }

    @Override
    protected void fillInsertStatement(Post post, List<Pair<Object, Integer>> params) {
        params.add(new Pair<>(post.getTitle(), Types.VARCHAR));
        params.add(new Pair<>(post.getContent(), Types.VARCHAR));
        params.add(new Pair<>(post.getType() == null ? null : post.getType().name(), Types.VARCHAR));
        params.add(new Pair<>(post.getAuthor() == null ? null : post.getAuthor().getId(), Types.INTEGER));
        params.add(new Pair<>(post.getSection() == null ? null : post.getSection().getId(), Types.INTEGER));
    }
}